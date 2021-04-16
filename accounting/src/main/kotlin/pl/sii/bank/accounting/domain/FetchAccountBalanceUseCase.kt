package pl.sii.bank.accounting.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class FetchAccountBalanceUseCase(
    private val accountBalanceStore: AccountBalanceStore,
    private val log: Logger = LoggerFactory.getLogger(FetchAccountBalanceUseCase::class.java)
) {

    fun execute(input: Input): Output? {
        log.info("Fetching balance for account {}", input.accountId)
        return accountBalanceStore.fetchBalance(input.accountId)?.let { balance ->
            Output(input.accountId, balance)
        }
    }

    data class Input(
        val accountId: UUID
    )

    data class Output(
        val accountId: UUID,
        val balance: Money
    )
}
