package pl.sii.bank.transaction.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class InitializeTransactionUseCase(
    private val transactionStore: TransactionStore,
    private val log: Logger = LoggerFactory.getLogger(InitializeTransactionUseCase::class.java)
) {
    fun execute(input: Input): Output {
        log.info("Initializing transaction")
        val command = InitializeTransaction(input.toAccount, input.money, input.note)
        return Transaction.initialize(command).let {
            log.info("Saving initialized transaction {}", it.id)
            val savedTransaction = transactionStore.save(it)
            Output(savedTransaction.id)
        }
    }

    data class Input(
        val toAccount: UUID,
        val money: MonetaryValue,
        val note: String?
    )

    data class Output(
        val transactionId: UUID
    )
}
