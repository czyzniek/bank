package pl.sii.bank.transaction.domain

import java.util.*

class AuthorizeTransactionUseCase(
    private val transactionStore: TransactionStore,
    private val customerAccountStore: CustomerAccountStore,
    private val externalTransferProvider: ExternalTransferProvider
) {

    fun execute(input: Input): Output {
        val authorizedTransaction = transactionStore.find(input.transactionId)
            ?.authorize()
            ?.apply {
                val submittedTransferId = submitTransactionToExternalProvider(this)
                this.submittedTransferId = submittedTransferId
            }
            ?.apply {
                transactionStore.save(this)
            }
        return Output(authorizedTransaction != null)
    }

    private fun submitTransactionToExternalProvider(transaction: Transaction): UUID {
        val fromAccount = customerAccountStore.findById(transaction.fromAccount!!)
        val toAccount = customerAccountStore.findById(transaction.toAccount)
        val params = ExternalTransferProvider.SubmitTransferParams(
            fromAccount = fromAccount!!.iban,
            toAccount = toAccount!!.iban,
            amount = transaction.money.amount,
            currency = transaction.money.currency.name,
            note = transaction.note
        )
        val result = externalTransferProvider.submitTransfer(params)
        return result.externalId
    }

    data class Input(
        val transactionId: UUID
    )

    data class Output(
        val success: Boolean
    )
}
