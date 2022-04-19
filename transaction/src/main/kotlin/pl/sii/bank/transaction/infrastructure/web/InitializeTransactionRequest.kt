package pl.sii.bank.transaction.infrastructure.web

import java.math.BigDecimal
import java.util.UUID

data class InitializeTransactionRequest(
    val targetAccount: UUID,
    val money: MonetaryValue,
    val note: String?
)

data class MonetaryValue(
    val amount: BigDecimal,
    val currency: String
)
