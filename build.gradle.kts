import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "2.7.0" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
}

allprojects {
    repositories {
        mavenCentral()
    }

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "io.spring.dependency-management")
}

subprojects {
    group = "io.hugo"
    version = "0.0.1-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_11

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom(SpringBootPlugin.BOM_COORDINATES)
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.3")
            mavenBom("org.springframework.data:spring-data-bom:2021.2.0")
            mavenBom("io.temporal:temporal-bom:1.23.0")
        }
    }

    dependencies {
        implementation(kotlin("reflect"))
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        implementation("org.javamoney:moneta:1.4.2")
        implementation("org.zalando:jackson-datatype-money:1.3.0")
        implementation("nl.hiddewieringa:money-kotlin:1.0.1")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.findByName("bootBuildImage")?.let {
        with(it as BootBuildImage) {
            imageName = "app-${project.name}"
            // Refers to https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/
            // Refers to https://paketo.io/docs/howto/java
            environment = mapOf(
                "BP_JVM_VERSION" to "11.*"
            )
        }
    }
}
