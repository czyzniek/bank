package pl.sii.bank.transaction.domain

import java.math.BigDecimal
import java.time.Instant
import java.util.*

class Transaction(
    val id: UUID,
    var fromAccount: UUID?,
    val toAccount: UUID,
    val money: MonetaryValue,
    val note: String?,
    var status: TransactionStatus,
    val createdAt: Instant,
    var authorizedAt: Instant?
) {

    companion object Factory {
        fun initialize(command: InitializeTransaction): Transaction =
            Transaction(
                UUID.randomUUID(),
                null,
                command.toAccount,
                command.money,
                command.note,
                TransactionStatus.INITIALIZED,
                Instant.now(),
                null
            )
    }

    fun confirm(command: ConfirmTransaction): Transaction =
        apply {
            fromAccount = command.fromAccount
            status = TransactionStatus.CONFIRMED
        }

    fun authorize(): Transaction =
        apply {
            authorizedAt = Instant.now()
            status = TransactionStatus.AUTHORIZED
        }
}

data class MonetaryValue(
    val amount: BigDecimal,
    val currency: Currency
)

enum class Currency {
    EUR, PLN, USD
}

enum class TransactionStatus {
    INITIALIZED, CONFIRMED, AUTHORIZED
}

data class InitializeTransaction(
    val toAccount: UUID,
    val money: MonetaryValue,
    val note: String?
)

data class ConfirmTransaction(
    val fromAccount: UUID
)