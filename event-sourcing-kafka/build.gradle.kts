dependencies {
    implementation(project(":common"))
    implementation(project(":event-sourcing"))
    implementation("org.apache.kafka:kafka-clients")
    implementation("org.springframework:spring-beans")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.kafka:spring-kafka")
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")
}
