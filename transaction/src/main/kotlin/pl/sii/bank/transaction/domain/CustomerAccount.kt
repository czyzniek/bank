package pl.sii.bank.transaction.domain

import java.util.UUID

data class CustomerAccount(
    val id: UUID,
    val iban: String,
    val currency: Currency,
    val customerId: UUID
)
