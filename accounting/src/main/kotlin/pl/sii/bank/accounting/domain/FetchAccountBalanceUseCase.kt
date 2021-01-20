package pl.sii.bank.accounting.domain

import java.util.*

class FetchAccountBalanceUseCase(private val accountBalanceStore: AccountBalanceStore) {

    fun execute(input: Input): Output? =
        accountBalanceStore.fetchBalance(input.accountId)?.let { balance ->
            Output(input.accountId, balance)
        }

    data class Input(
        val accountId: UUID
    )

    data class Output(
        val accountId: UUID,
        val balance: Money
    )
}
