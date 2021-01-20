import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.cloud.contract.verifier.config.TestFramework
import org.springframework.cloud.contract.verifier.plugin.ContractVerifierExtension

plugins {
    id("idea")
    id("groovy")
    id("maven-publish")
    id("org.springframework.cloud.contract") version "2.2.5.RELEASE"
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
    kotlin("plugin.allopen") version "1.4.21"
}

group = "pl.sii.bank"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "Hoxton.SR9"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-rabbit")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
        exclude(module = "mockito-junit-jupiter")
    }
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-support")
    testImplementation("io.mockk:mockk:1.10.3")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configure<ContractVerifierExtension> {
    setContractsDslDir(file("src/main/contracts"))
    setTestFramework(TestFramework.JUNIT5)
    setBasePackageForTests("pl.sii.bank.accounting")
    setBaseClassMappings(
        mapOf(
            "web" to "pl.sii.bank.accounting.WebContractBaseClass",
            "messaging" to "pl.sii.bank.accounting.MessagingContractBaseClass"
        )
    )
    setFailOnInProgress(false)
}
