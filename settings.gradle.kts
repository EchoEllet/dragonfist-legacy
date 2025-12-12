pluginManagement {
    repositories {
        fun strictMaven(name: String, url: String, vararg includeGroups: String) {
            exclusiveContent {
                forRepository { maven { this.name = name; this.url = uri(url) } }
                filter { includeGroups.forEach { includeGroup(it) } }
            }
        }

        fun strictMaven(name: String, url: String, filter: InclusiveRepositoryContentDescriptor.() -> Unit) {
            exclusiveContent {
                forRepository { maven { this.name = name; this.url = uri(url) } }
                filter { filter() }
            }
        }

        gradlePluginPortal()

        strictMaven(
            "NeoForged",
            "https://maven.neoforged.net/releases",
            "net.neoforged"
        )
        strictMaven(
            "Fabric",
            "https://maven.fabricmc.net/",
        ) {
            @Suppress("UnstableApiUsage")
            includeGroupAndSubgroups("net.fabricmc")
            includeGroup("fabric-loom")
        }
    }
    includeBuild("gradle/build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "DragonFist"

include(
    ":common",
    ":neoforge",
    ":fabric",
)
