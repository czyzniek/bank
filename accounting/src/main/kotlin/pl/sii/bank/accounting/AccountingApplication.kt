package pl.sii.bank.accounting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AccountingApplication

fun main(args: Array<String>) {
    runApplication<AccountingApplication>(*args)
}
