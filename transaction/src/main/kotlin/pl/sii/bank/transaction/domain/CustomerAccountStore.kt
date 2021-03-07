package pl.sii.bank.transaction.domain

import java.util.*

interface CustomerAccountStore {
    fun save(account: CustomerAccount): CustomerAccount

    fun findById(id: UUID): CustomerAccount?

    fun findByCustomerId(customerId: UUID): CustomerAccount?
}
