dependencies {
    implementation(project(":common"))
    implementation(project(":model"))
    implementation("javax.servlet:javax.servlet-api:3.1.0")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.vladmihalcea:hibernate-types-52:2.16.2")

    // For Sync APIs
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // For Async APIs
    compileOnly("org.springframework.boot:spring-boot-starter-data-r2dbc")
    compileOnly("io.r2dbc:r2dbc-postgresql:0.8.12.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
