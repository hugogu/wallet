dependencies {
    implementation(project(":common-tools"))
    implementation(project(":model"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.apache.flink:flink-streaming-java:1.19.0")
    implementation("org.apache.flink:flink-clients:1.19.0")
    implementation("org.apache.flink:flink-connector-kafka:3.0.2-1.18")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    runtimeOnly("org.yaml:snakeyaml")
}

// Conflicts with Flink's log4j2 dependency
configurations.implementation {
    exclude(group = "ch.qos.logback", module = "*")
}

tasks.jar {
    isZip64 = true
    manifest.attributes["Main-Class"] = "FraudDetection"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/*")
    exclude("org/apache/flink/*")
    exclude("org/scala-lang/*")
}

