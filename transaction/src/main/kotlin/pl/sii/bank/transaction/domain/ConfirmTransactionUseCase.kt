package pl.sii.bank.transaction.domain

import java.util.*

class ConfirmTransactionUseCase(private val transactionStore: TransactionStore) {

    fun execute(input: Input): Output {
        val confirmedTransaction = transactionStore.find(input.transactionId)
            ?.confirm(ConfirmTransaction(input.fromAccount))
            ?.apply {
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