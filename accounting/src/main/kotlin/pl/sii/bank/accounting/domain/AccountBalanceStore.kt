package pl.sii.bank.accounting.domain

import java.util.UUID

interface AccountBalanceStore {

    fun fetchBalance(accountId: UUID): Money?
}
