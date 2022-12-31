dependencies {
    implementation(project(":common"))
    implementation(project(":event-sourcing"))
    implementation("com.vladmihalcea:hibernate-types-52:2.16.2")
    implementation("javax.servlet:javax.servlet-api:3.1.0")
    implementation("org.springframework:spring-beans")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
