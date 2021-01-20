package pl.sii.bank.bankprovider

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BankProviderApplication

fun main(args: Array<String>) {
    runApplication<BankProviderApplication>(*args)
}
