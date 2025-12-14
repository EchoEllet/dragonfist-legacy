plugins {
    id("multiloader-platform")
    alias(libs.plugins.fabricloom)
}

configureBaseArchive("fabric")

repositories {
    parchmentMcRepository()
    strictMaven("Terraformers", "https://maven.terraformersmc.com", "com.terraformersmc")
}

dependencies {
    minecraft(libs.fabric.minecraft)
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${mcVersion}:${parchmentMc}@zip")
    })
    modImplementation(libs.fabric.loader)

    // Fabric API
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.kotlin)
    modImplementation(libs.modmenu.fabric)

    modImplementation(libs.forgeconfigapiport.fabric) {
        exclude(group = libs.fabric.loader.get().group)
        exclude(group = libs.fabric.api.get().group)
    }
}

loom {
    // Removed due to lack of NeoForge support: https://github.com/neoforged/ModDevGradle/issues/309
//    splitEnvironmentSourceSets()

    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
//            sourceSet(sourceSets.named("client").get())
        }
    }
}

tasks.processResources {
    val replaceProperties = modPlatformMetadataReplaceProperties

    inputs.properties(replaceProperties)

    filesMatching("fabric.mod.json") {
        expand(replaceProperties)
    }
}

configureModPublish(
    modLoader = "fabric",
    isForgeLike = false,
) { tasks.remapJar.get().archiveFile }
