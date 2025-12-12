import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.BasePluginExtension
import java.net.URI

fun RepositoryHandler.strictMaven(name: String, url: String, vararg includeGroups: String) {
    exclusiveContent {
        forRepository { maven { this.name = name; this.url = URI(url) } }
        filter { includeGroups.forEach { includeGroup(it) } }
    }
}

fun RepositoryHandler.parchmentMcRepository() {
    strictMaven(
        "ParchmentMC",
        "https://maven.parchmentmc.org",
        "org.parchmentmc",
    )
}

const val generationTaskGroup = "generation"

private val Project.versionCatalog: VersionCatalog
    get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

private fun Project.gradleProperty(name: String): String {
    return this.providers.gradleProperty(name).get()
}

val Project.modVersion: String
    get() = catalogVersion("mod")

private val Project.modLicense: String
    get() = gradleProperty("license")

val Project.modId: String
    get() = gradleProperty("modId")

val Project.mcVersion: String
    get() = catalogVersion("minecraft")

val Project.parchmentMc: String
    get() = catalogVersion("parchmentMappings")

val Project.javaVersion: Int
    get() = catalogVersion("java").toInt()

private val Project.modIssueTracker: String
    get() = gradleProperty("issueTracker")

private val Project.modDisplayName: String
    get() = gradleProperty("displayName")

private val Project.modDisplayUrl: String
    get() = gradleProperty("displayUrl")

private val Project.modCredits: String
    get() = gradleProperty("credits")

private val Project.modDescription: String
    get() = gradleProperty("description")

private val Project.modAuthors: String
    get() = gradleProperty("authors")

fun Project.catalogVersion(name: String): String = versionCatalog.findVersion(name).get().toString()

fun Project.configureBaseArchive(variant: String) {
    extensions.getByType(BasePluginExtension::class.java).apply {
        archivesName.set(modId)
        version = "${modVersion}-mc${mcVersion}-$variant"
    }
}

val Project.modPlatformMetadataReplaceProperties
    get() = mapOf(
        "minecraftVersionRange" to catalogVersion("minecraftRange"),
        "neoforgeVersionRange" to catalogVersion("neoforgeRange"),
        "forgeLoaderVersionRange" to catalogVersion("kotlinforforgeLoaderRange"),
        "epicfightVersionRange" to catalogVersion("epicfightRange"),
        "epicskillsVersionRange" to catalogVersion("epicskillsRange"),
        "modId" to modId,
        "modVersion" to modVersion,
        "license" to modLicense,
        "issueTracker" to modIssueTracker,
        "displayName" to modDisplayName,
        "displayUrl" to modDisplayUrl,
        "credits" to modCredits,
        "description" to modDescription,
        "authors" to modAuthors,
        "sourceCode" to modDisplayUrl,
        "fabricLoader" to catalogVersion("fabric-loader"),
        "minecraft" to mcVersion,
        "javaVersion" to javaVersion,
        "forgeconfigapiportVersion" to catalogVersion("forgeconfigapiport"),
    )
