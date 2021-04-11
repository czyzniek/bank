package pl.sii.bank.transaction.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class ConfirmTransactionUseCase(
    private val transactionStore: TransactionStore,
    private val accountBalanceStore: AccountBalanceStore,
    private val log: Logger = LoggerFactory.getLogger(ConfirmTransactionUseCase::class.java)
) {
    fun execute(input: Input): Output {
        log.info("Confirming transaction with id {}", input.transactionId)
        log.info("Fetching balance for fromAccount {}", input.fromAccount)
        val fromAccountBalance = accountBalanceStore.fetchAccountBalance(input.fromAccount)
        val confirmedTransaction = transactionStore.find(input.transactionId)
            ?.confirm(ConfirmTransaction(input.fromAccount, fromAccountBalance.balance))
            ?.apply {
                log.info("Saving confirmed transaction {}", input.transactionId)
                transactionStore.save(this)
            }
        return Output(confirmedTransaction != null)
    }

    data class Input(
        val transactionId: UUID,
        val fromAccount: UUID
    )

    data class Output(
        val success: Boolean
    )
}
