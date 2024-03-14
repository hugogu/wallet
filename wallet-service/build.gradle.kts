dependencies {
    implementation(project(":data-model"))
    implementation(project(":event-sourcing-kafka"))
    implementation(project(":event-sourcing-mvc"))
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.vladmihalcea:hibernate-types-52:2.16.2")
    runtimeOnly("org.postgresql:postgresql")
}
