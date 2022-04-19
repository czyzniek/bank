package pl.sii.bank.transaction.infrastructure.store

import pl.sii.bank.transaction.domain.Transaction
import pl.sii.bank.transaction.domain.TransactionStore
import java.util.UUID
import java.util.concurrent.ConcurrentMap

class InMemoryTransactionStore(private val db: ConcurrentMap<UUID, Transaction>) : TransactionStore {
    override fun save(trx: Transaction): Transaction =
        trx.apply { db[id] = this }

    override fun find(id: UUID): Transaction? = db[id]
}
