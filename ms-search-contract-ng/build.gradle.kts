plugins {
    `java-library`
}

apply(plugin = "com.mahisoft.kamino.ng.kotlin.library")

group = "com.mahisoft.kamino"

tasks.withType<JacocoCoverageVerification> {
    violationRules {
        isFailOnViolation = false
    }
}

//Otherwise project does not work with publishToMavenLocal
configure<PublishingExtension> {
    publications {
        repositories {
            mavenLocal()
        }
    }
}
val kaminoVersion = "1.2.35-SNAPSHOT"

dependencies {
    compile("com.mahisoft.kamino:commons-rest:$kaminoVersion")
}
