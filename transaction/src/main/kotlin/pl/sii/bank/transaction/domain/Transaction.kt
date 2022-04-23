package pl.sii.bank.transaction.domain

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class Transaction(
    val id: UUID,
    val type: TransactionType,
    var fromAccount: UUID?,
    val toAccount: UUID,
    val money: MonetaryValue,
    val note: String?,
    var status: TransactionStatus,
    val createdAt: Instant,
    var authorizedAt: Instant?,
    var submittedTransfersId: List<UUID>
) {

    companion object Factory {
        fun initialize(command: InitializeTransaction): Transaction =
            Transaction(
                UUID.randomUUID(),
                command.type,
                null,
                command.toAccount,
                command.money,
                command.note,
                TransactionStatus.INITIALIZED,
                Instant.now(),
                null,
                mutableListOf()
            )
    }

    fun getFee(): MonetaryValue =
        MonetaryValue(
            money.amount.multiply(type.fee.rate),
            money.currency
        )

    fun confirm(command: ConfirmTransaction): Transaction =
        apply {
            val totalTransactionAmount = calculateTotalAmount()
            if (command.fromAccountBalance.currency != totalTransactionAmount.currency ||
                command.fromAccountBalance.amount.compareTo(totalTransactionAmount.amount) == -1
            ) {
                throw IllegalStateException("Account does not have enough money!")
            }

            fromAccount = command.fromAccount
            status = TransactionStatus.CONFIRMED
        }

    fun authorize(): Transaction =
        apply {
            authorizedAt = Instant.now()
            status = TransactionStatus.AUTHORIZED
        }

    private fun calculateTotalAmount(): MonetaryValue {
        val feeAmount = MonetaryValue(money.amount * type.fee.rate, money.currency)
        return MonetaryValue(
            money.amount + feeAmount.amount,
            money.currency
        )
    }
}

enum class TransactionType(val fee: Fee) {
    STANDARD(Fee(BigDecimal("0.01"))),
    PREMIUM(Fee(BigDecimal("0.1")));
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
    val type: TransactionType,
    val toAccount: UUID,
    val money: MonetaryValue,
    val note: String?
)

data class ConfirmTransaction(
    val fromAccount: UUID,
    val fromAccountBalance: MonetaryValue
)

data class Fee(
    val rate: BigDecimal
)
