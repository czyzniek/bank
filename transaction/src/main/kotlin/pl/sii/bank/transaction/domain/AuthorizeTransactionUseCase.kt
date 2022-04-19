package pl.sii.bank.transaction.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

class AuthorizeTransactionUseCase(
    private val transactionStore: TransactionStore,
    private val customerAccountStore: CustomerAccountStore,
    private val externalTransferProvider: ExternalTransferProvider,
    private val log: Logger = LoggerFactory.getLogger(AuthorizeTransactionUseCase::class.java)
) {
    fun execute(input: Input): Output {
        log.info("Authorizing transaction with id {}", input.transactionId)
        val authorizedTransaction = transactionStore.find(input.transactionId)
            ?.authorize()
            ?.apply {
                log.info("Submitting transaction {} to external provider", input.transactionId)
                val submittedTransferId = submitTransactionToExternalProvider(this)
                this.submittedTransferId = submittedTransferId
            }
            ?.apply {
                log.info("Saving authorized transaction {}", input.transactionId)
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
