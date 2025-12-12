plugins {
    id("multiloader-base")
}

val commonModule: Project = project(":common")

dependencies {
    compileOnly(commonModule)
}

tasks.compileKotlin { source(commonModule.sourceSets.main.get().allSource) }
tasks.compileJava { source(commonModule.sourceSets.main.get().allSource) }

val commonProcessResources = commonModule.tasks.processResources

tasks.processResources {
    dependsOn(commonProcessResources)
    from(commonProcessResources.map { it.outputs.files })
}
