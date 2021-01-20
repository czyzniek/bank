package pl.sii.bank.accounting.domain

import java.math.BigDecimal

enum class Currency {
    PLN, USD, EUR
}

data class Money(
    val amount: BigDecimal,
    val currency: Currency
)
