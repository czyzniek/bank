import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("idea")
    id("groovy")
    id("maven-publish")
    id("org.springframework.cloud.contract") version "3.0.1" apply false
    id("org.springframework.boot") version "2.4.3" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    kotlin("jvm") version "1.4.31" apply false
    kotlin("plugin.spring") version "1.4.31" apply false
}

subprojects {
    group = "pl.sii.bank"
    version = "0.0.1-SNAPSHOT"

    apply {
        plugin("idea")
        plugin("groovy")
        plugin("io.spring.dependency-management")
        plugin("org.springframework.boot")
        plugin("org.springframework.cloud.contract")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    extra["springCloudVersion"] = "2020.0.1"

    configure<DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        }

        dependencies {
            dependency("org.spockframework:spock-spring:1.3-groovy-2.5")
            dependency("io.github.joke:spock-mockable:1.4.0")
        }
    }
}
