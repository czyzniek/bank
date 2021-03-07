package pl.sii.bank.transaction.infrastructure.store

import pl.sii.bank.transaction.domain.CustomerAccount
import pl.sii.bank.transaction.domain.CustomerAccountStore
import java.util.*
import java.util.concurrent.ConcurrentMap

class InMemoryCustomerAccountStore(private val db: ConcurrentMap<UUID, CustomerAccount>) : CustomerAccountStore {
    override fun save(account: CustomerAccount): CustomerAccount =
        account.apply { db[id] = this }

    override fun findById(id: UUID): CustomerAccount? =
        db[id]

    override fun findByCustomerId(customerId: UUID): CustomerAccount? =
        db.values
            .find { customerAccount -> customerAccount.customerId == customerId }
}
