package pl.sii.bank.transaction.domain

import java.util.*

interface TransactionStore {
    fun save(trx: Transaction): Transaction

    fun find(id: UUID): Transaction?
}