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

/**
 * Copy dependencies to be included in flink /opt/flink/lib directory.
 * Not all libraries are appropriate for this, so we filter out some of them.
 * For those already included in flink-dist, we can exclude them.
 */
tasks.register<Copy>("copyDependencies") {
    from(configurations.runtimeClasspath.get().filter {
        it.name.startsWith("jackson")
                || it.name.startsWith("kotlin")
                || it.name.startsWith("kafka")
                || it.name.startsWith("spring")
                || it.name.startsWith("snakeyaml")
                || it.name.startsWith("jakarta")
                || it.name.startsWith("zstd-jni")
                || it.name.startsWith("money-api")
                || it.name.startsWith("moneta")
                || it.name.startsWith("hibernate")
    })
    into("libs")
}
