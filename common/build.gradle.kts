import dev.echoellet.mc_safe_resources.GenerateJsonKeysTask
import dev.echoellet.mc_safe_resources.OutputLanguage

plugins {
    id("multiloader-base")
    alias(libs.plugins.moddevgradle)
    alias(libs.plugins.mcSafeResources)
}

configureBaseArchive("common")

neoForge {
    neoFormVersion = libs.versions.neoform.get()

    parchment {
        mappingsVersion.set(libs.versions.parchmentMappings.get())
        minecraftVersion.set(libs.versions.minecraft.get())
    }
}

dependencies {
    testImplementation(libs.kotlin.test)
    compileOnly(libs.forgeconfigapiport.common)
    compileOnly(libs.mixin)
}

tasks.test {
    useJUnitPlatform()
}

// Keep in sync with the `BanditRank` enum in the source code.
// Each `entityId` must match the corresponding registered entity ID.
enum class BanditRank(val entityId: String) {
    Regular("bandit_regular"),
    Enforcer("bandit_enforcer"),
    Champion("bandit_champion"),
    Elite("bandit_elite"),
    Leader("bandit_leader"),
    Ruler("bandit_ruler");

    fun getAttributes(): EpicFightMobAttributes = when (this) {
        Regular -> EpicFightMobAttributes(impact = 0.4, chasingSpeed = 1.0)
        Enforcer -> EpicFightMobAttributes(impact = 0.425, maxStrikes = 2, armorNegation = 1.0, chasingSpeed = 1.05)
        Champion -> EpicFightMobAttributes(impact = 0.45, maxStrikes = 2, armorNegation = 2.0, chasingSpeed = 1.010)
        Elite -> EpicFightMobAttributes(
            impact = 0.455,
            maxStrikes = 3,
            scale = 1.050,
            armorNegation = 5.0,
            chasingSpeed = 1.015
        )

        Leader -> EpicFightMobAttributes(
            impact = 0.5,
            maxStrikes = 4,
            scale = 1.075,
            armorNegation = 10.0,
            chasingSpeed = 1.1
        )

        Ruler -> EpicFightMobAttributes(
            impact = 1.0,
            maxStrikes = 6,
            scale = 1.085,
            armorNegation = 20.0,
            chasingSpeed = 1.2
        )
    }
}

data class EpicFightMobAttributes(
    val impact: Double,
    val armorNegation: Double = 0.0,
    val maxStrikes: Int = 1,
    val chasingSpeed: Double = 1.0,
    val scale: Double = 1.0,
)

val generateBanditEpicFightMobPatchFiles = tasks.register("generateBanditEpicFightMobPatchFiles") {
    group = generationTaskGroup
    description = "Generates Bandit Epic Fight mob patch JSON files for different ranks from the same template."

    val taskName = name

    val inputDir = layout.projectDirectory.dir("src/main/templates/epicfight_mobpatch")
    val outputDir = layout.buildDirectory.dir("generated/$taskName/src/main/resources")

    inputs.dir(inputDir)
    outputs.dir(outputDir)

    // Required for Gradle configuration caching; using `modId` directly inside `doLast` can cause the build to fail.
    val cachedModId = modId

    doLast {
        val templateFile = inputDir.file("bandit_template.json").asFile
        val templateContent = templateFile.readText().lines()
            .filterNot { it.trimStart().startsWith("\"_comment\"") }
            .joinToString("\n")

        for (rank in BanditRank.entries) {

            fun String.replacePlaceholder(name: String, value: Any) =
                this.replaceFirst($$"\"${$$name}\"", value.toString())

            val outputFile = outputDir.get().file("data/$cachedModId/epicfight_mobpatch/${rank.entityId}.json").asFile
            val attributes = rank.getAttributes()
            val fileContent = templateContent
                .replacePlaceholder("impact", attributes.impact)
                .replacePlaceholder("armorNegation", attributes.armorNegation)
                .replacePlaceholder("maxStrikes", attributes.maxStrikes)
                .replacePlaceholder("chasingSpeed", attributes.chasingSpeed)
                .replacePlaceholder("scale", attributes.scale)

            outputFile.parentFile.mkdirs()
            outputFile.writeText(fileContent)
        }
    }
}

sourceSets.main {
    resources.srcDir(generateBanditEpicFightMobPatchFiles.map { it.outputs.files })
}

tasks.processResources {
    dependsOn(generateBanditEpicFightMobPatchFiles)
}

val generatedPackageName = "$group.${modId}.generated"
val modAssetsDirPath = "src/main/resources/assets/${modId}"
fun getGenOutputDirPath(taskName: String) = "generated/$taskName/src/main/kotlin"

val generateModAssetPaths = tasks.register("generateModAssetPaths") {
    group = generationTaskGroup
    description = "Generates Kotlin object represents the mod asset paths for type-safety"

    val taskName = name

    val inputDir = layout.projectDirectory.dir(modAssetsDirPath)
    val outputDir = layout.buildDirectory.dir(getGenOutputDirPath(taskName))

    inputs.dir(inputDir).withPathSensitivity(PathSensitivity.RELATIVE)
    outputs.dir(outputDir)

    val className = "ModAssetPaths"
    val generatedPackageName = generatedPackageName

    doLast {
        val modAssetsDir = inputDir.asFile

        if (!modAssetsDir.exists()) return@doLast

        val constants = mutableListOf<String>()

        fun walkAssets(dir: File, relativePath: String = "") {
            dir.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    walkAssets(file, "$relativePath${file.name}/")
                } else {
                    val name = file.name
                    val constName = (relativePath + name)
                        .replace("[^A-Za-z0-9]".toRegex(), "_")
                        .uppercase()
                    val value = "$relativePath${name}"
                    constants += "    const val $constName = \"$value\""
                }
            }
        }

        walkAssets(modAssetsDir)

        val classFile = outputDir.get().asFile.resolve("$className.kt")

        classFile.apply {
            parentFile.mkdirs()
            writeText(
                """
                |package $generatedPackageName
                |
                | // Generated code, don't modify directly. Update the task "$taskName" in build.gradle.kts if needed.
                |object $className {
                |${constants.joinToString("\n")}
                |}
            """.trimMargin()
            )
        }

        println("Generated `$className`: ${classFile.path}")
    }
}

mcSafeResources {
    namespace.set(modId)
    outputLanguage.set(KOTLIN)
    keyStringReplacements.set(
        mapOf($$"${modId}" to modId)
    )
}

val generateSoundNames = tasks.register<GenerateJsonKeysTask>("generateSoundNames") {
    val resourcePath = "$modAssetsDirPath/sounds.json"

    inputResourceFile.set(project.file(resourcePath))
    outputClassName.set("SoundNames")
    outputLanguage.set(OutputLanguage.KOTLIN)
    outputClassDescription.set(buildString {
        append("A generated object that represents the keys in `$resourcePath` resource file,"); appendLine()
        append("to avoid hardcoding the keys across the codebase, which is error-prone, inefficient, and less type-safe.")
    })
    outputPackage.set(generatedPackageName)
    keyNamespaceToStrip.set(modId)
}

kotlin {
    sourceSets.main.get().kotlin.srcDir(generateModAssetPaths.map { it.outputs.files.singleFile })
    sourceSets.main.get().kotlin.srcDir(tasks.generateLangKeys.map { it.outputs.files.singleFile })
    sourceSets.main.get().kotlin.srcDir(generateSoundNames.map { it.outputs.files.singleFile })
}

tasks.compileKotlin {
    dependsOn(generateModAssetPaths)
    dependsOn(tasks.generateLangKeys)
    dependsOn(generateSoundNames)
}

tasks.processResources {
    val cachedModId = modId

    val replaceProperties = mapOf(
        "modId" to cachedModId
    )

    inputs.properties(replaceProperties)
    val patterns = listOf(
        "assets/$cachedModId/lang/*.json",
        "assets/$cachedModId/sounds.json",
        "data/$cachedModId/tags/entity_type/*.json",
    )
    inputs.property("patterns", patterns)

    patterns.forEach { pattern ->
        filesMatching(pattern) {
            expand(replaceProperties)
        }
    }
}
