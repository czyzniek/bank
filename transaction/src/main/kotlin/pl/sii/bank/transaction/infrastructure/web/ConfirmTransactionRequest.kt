package pl.sii.bank.transaction.infrastructure.web

import java.util.UUID

data class ConfirmTransactionRequest(
    val sourceAccount: UUID
)
