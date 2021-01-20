package pl.sii.bank.transaction.infrastructure.web

import java.util.*

data class ConfirmTransactionRequest(
    val sourceAccount: UUID
)
