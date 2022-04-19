package pl.sii.bank.transaction.domain

import java.math.BigDecimal
import java.util.UUID

interface ExternalTransferProvider {
    fun submitTransfer(params: SubmitTransferParams): Result

    data class SubmitTransferParams(
        val fromAccount: String,
        val toAccount: String,
        val amount: BigDecimal,
        val currency: String,
        val note: String?,
    )

    data class Result(
        val externalId: UUID
    )
}
