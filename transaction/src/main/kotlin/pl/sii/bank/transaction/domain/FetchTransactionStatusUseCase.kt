package pl.sii.bank.transaction.domain

import java.util.UUID

class FetchTransactionStatusUseCase(private val transactionStore: TransactionStore) {

    fun execute(input: Input): Output? =
        transactionStore.find(input.transactionId)
            ?.let {
                Output(it.status)
            }

    data class Input(
        val transactionId: UUID
    )

    data class Output(
        val status: TransactionStatus
    )
}
