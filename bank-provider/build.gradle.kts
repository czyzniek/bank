import org.springframework.cloud.contract.verifier.plugin.ContractVerifierExtension

plugins {
    id("maven-publish")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
}

configure<ContractVerifierExtension> {
    setContractsDslDir(file("src/main/contracts"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks.named("verifierStubsJar"))
        }
    }
}

tasks.replace("check").doLast {
    println("There is nothing to check in bank-provider!")
}
