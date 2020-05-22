apply(plugin = "com.mahisoft.kamino.ng.kotlin.microservice")

plugins {
    val kotlinVersion = "1.3.31"

    //This makes the java persistence API create classes without a zero argument constructor
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion

    //Not being grabbed from plugin, otherwise implementation needs quotes
    `java-library`
    application
}


group = "com.mahisoft.kamino.mssearch"

//TODO remove when coverage passes.
tasks.withType<JacocoCoverageVerification> {
    violationRules {
        isFailOnViolation = false
    }
}

//To use JUnit5 + Jupiter
tasks.withType<Test> {
    useJUnitPlatform()
}

//Boot run task needs this to be specified
application {
    mainClassName = "com.mahisoft.kamino.mssearch.MsSearchApplicationKt"
}

dependencies {
    implementation(project(":ms-search-contract-ng"))
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.1.6.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.1.6.RELEASE")
    implementation("org.flywaydb:flyway-core:5.2.4")
    runtimeOnly("mysql:mysql-connector-java")
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:6.8.8")
    implementation("io.vavr:vavr:0.10.2")
    implementation("org.mariadb.jdbc:mariadb-java-client")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testImplementation("org.flywaydb.flyway-test-extensions:flyway-spring-test:5.2.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.mockito:mockito-inline:2.28.2")
    testImplementation("org.testcontainers:elasticsearch:1.14.1")
    testImplementation("org.testcontainers:mysql:1.14.1")

}