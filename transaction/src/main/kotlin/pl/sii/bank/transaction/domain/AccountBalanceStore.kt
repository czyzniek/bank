package pl.sii.bank.transaction.domain

import java.util.UUID

interface AccountBalanceStore {

    fun fetchAccountBalance(accountId: UUID): AccountBalance

    data class AccountBalance(
        val accountId: UUID,
        val balance: MonetaryValue
    )
}
