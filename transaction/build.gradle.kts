import org.springframework.cloud.contract.verifier.config.TestFramework
import org.springframework.cloud.contract.verifier.plugin.ContractVerifierExtension

plugins {
    id("maven-publish")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-rabbit")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
        exclude(module = "mockito-junit-jupiter")
    }
    testImplementation("org.springframework.cloud:spring-cloud-stream") {
        artifact {
            name = "spring-cloud-stream"
            extension = "jar"
            type = "test-jar"
            classifier = "test-binder"
        }
    }
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
    testImplementation("org.spockframework:spock-spring")
    testImplementation("io.github.joke:spock-mockable")

}

configure<ContractVerifierExtension> {
    setContractsDslDir(file("src/main/contracts"))
    setTestFramework(TestFramework.SPOCK)
    setBasePackageForTests("pl.sii.bank.transaction")
    setBaseClassForTests("pl.sii.bank.transaction.WebContractBaseSpec")
}
