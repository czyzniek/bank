package pl.sii.bank.transaction.infrastructure.web

import pl.sii.bank.transaction.domain.TransactionType
import java.math.BigDecimal
import java.util.UUID

data class InitializeTransactionRequest(
    val type: TransactionType,
    val targetAccount: UUID,
    val money: MonetaryValue,
    val note: String?
)

data class MonetaryValue(
    val amount: BigDecimal,
    val currency: String
)
