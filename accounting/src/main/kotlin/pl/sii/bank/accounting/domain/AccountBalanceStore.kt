package pl.sii.bank.accounting.domain

import java.util.*

interface AccountBalanceStore {

    fun fetchBalance(accountId: UUID): Money?
}
