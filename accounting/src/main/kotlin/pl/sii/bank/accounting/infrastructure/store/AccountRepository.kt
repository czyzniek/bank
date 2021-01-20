package pl.sii.bank.accounting.infrastructure.store

import java.util.*
import java.util.concurrent.ConcurrentMap

class AccountRepository(private val db: ConcurrentMap<UUID, AccountEntity>) {

    fun save(accountEntity: AccountEntity): AccountEntity =
        accountEntity.also {
            db[it.id] = it
        }

    fun findById(accountId: UUID): AccountEntity? =
        db[accountId]

    fun findAllForCustomer(customerId: UUID): List<AccountEntity> =
        db.values
            .filter { accountEntity -> accountEntity.customerId == customerId }
}
