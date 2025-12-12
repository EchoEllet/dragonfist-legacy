plugins {
    `java-library`
    idea
    kotlin("jvm")
    id("com.diffplug.spotless")
}

version = modVersion
group = "dev.echoellet"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(javaVersion)
}

kotlin {
    jvmToolchain(javaVersion)
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

repositories {
    mavenCentral()

    // Modrinth maven: https://support.modrinth.com/en/articles/8801191-modrinth-maven
    // Note: Prefer Maven repositories provided by the mod dependencies over Modrinth.
    strictMaven(
        "Modrinth",
        "https://api.modrinth.com/maven",
        "maven.modrinth",
    )

    // Note: Prefer Modrinth or Maven repositories provided by the mod dependencies over Curse Maven.
    strictMaven(
        "CurseMaven",
        "https://beta.cursemaven.com",
        "curse.maven",
    )

    strictMaven(
        "Fuzs Mod Resources (Forge Config API)",
        "https://raw.githubusercontent.com/Fuzss/modresources/main/maven/",
        "fuzs.forgeconfigapiport",
    )
}

tasks.jar {
    from(rootProject.file("LICENSE"))
    from(rootProject.file("CREDITS.md"))
}

// TODO: Re-enable later
//spotless {
//    kotlin {
//        ktlint()
//    }
//}
