package pl.sii.bank.transaction.domain

import java.util.*

class AuthorizeTransactionUseCase(private val transactionStore: TransactionStore) {

    fun execute(input: Input): Output {
        val authorizedTransaction = transactionStore.find(input.transactionId)
            ?.authorize()
            ?.apply {
                transactionStore.save(this)
            }
        return Output(authorizedTransaction != null)
    }

    data class Input(
        val transactionId: UUID
    )

    data class Output(
        val success: Boolean
    )
}