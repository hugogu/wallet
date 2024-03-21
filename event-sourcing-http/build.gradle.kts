dependencies {
    implementation(project(":common"))
    implementation(project(":common-tools"))
    implementation(project(":event-sourcing"))
    implementation("javax.servlet:javax.servlet-api:3.1.0")
    implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")
    implementation("org.springframework:spring-beans")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
}
