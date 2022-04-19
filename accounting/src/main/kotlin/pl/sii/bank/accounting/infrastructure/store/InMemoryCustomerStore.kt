package pl.sii.bank.accounting.infrastructure.store

import pl.sii.bank.accounting.domain.Account
import pl.sii.bank.accounting.domain.Customer
import pl.sii.bank.accounting.domain.CustomerStore
import java.util.UUID
import java.util.concurrent.ConcurrentMap

class InMemoryCustomerStore(
    private val dbCustomers: ConcurrentMap<UUID, CustomerEntity>,
    private val accountRepository: AccountRepository
) : CustomerStore {
    override fun save(customer: Customer): Customer =
        customer.apply {
            dbCustomers[id] = CustomerEntity(id, externalId, firstName, lastName, birthDate)
            accounts
                .map { account -> AccountEntity(account.id, account.iban, account.currency, this.id) }
                .forEach { accountRepository.save(it) }
        }

    override fun findAll(): List<Customer> =
        dbCustomers.values
            .map { customerEntity ->
                val foundAccounts = accountRepository.findAllForCustomer(customerEntity.id)
                    .map { Account.create(it) }
                    .toMutableList()
                Customer.create(customerEntity, foundAccounts)
            }

    override fun findById(customerId: UUID): Customer? =
        dbCustomers[customerId]?.let { customerEntity ->
            val foundAccounts = accountRepository.findAllForCustomer(customerEntity.id)
                .map { Account.create(it) }
                .toMutableList()
            Customer.create(customerEntity, foundAccounts)
        }
}
