package pl.sii.bank.bankprovider

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.contract.stubrunner.server.EnableStubRunnerServer

@SpringBootApplication
@EnableStubRunnerServer
class BankProviderApplication

fun main(args: Array<String>) {
    runApplication<BankProviderApplication>(*args)
}
