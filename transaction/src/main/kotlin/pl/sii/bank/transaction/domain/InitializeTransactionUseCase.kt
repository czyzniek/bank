package pl.sii.bank.transaction.domain

import java.util.*

class InitializeTransactionUseCase(private val transactionStore: TransactionStore) {

    fun execute(input: Input): Output {
        val command = InitializeTransaction(input.toAccount, input.money, input.note)
        return Transaction.initialize(command).let {
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