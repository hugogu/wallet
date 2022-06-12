dependencies {
    implementation(project(":common"))
    implementation(project(":model"))
    implementation("org.springframework:spring-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.vladmihalcea:hibernate-types-52:2.16.2")
    implementation("io.r2dbc:r2dbc-postgresql:0.8.12.RELEASE")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
