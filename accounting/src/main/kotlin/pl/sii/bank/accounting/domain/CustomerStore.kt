package pl.sii.bank.accounting.domain

import java.util.*

interface CustomerStore {
    fun save(customer: Customer): Customer
    fun findAll(): List<Customer>
    fun findById(customerId: UUID): Customer?
}
