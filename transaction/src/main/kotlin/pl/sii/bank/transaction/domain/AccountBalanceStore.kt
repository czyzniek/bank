package pl.sii.bank.transaction.domain

import java.util.*

interface AccountBalanceStore {

    fun fetchAccountBalance(accountId: UUID): AccountBalance

    data class AccountBalance(
        val accountId: UUID,
        val balance: MonetaryValue
    )
}