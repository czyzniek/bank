package pl.sii.bank.accounting.infrastructure.store

import pl.sii.bank.accounting.domain.Currency
import java.util.UUID

data class AccountEntity(
    val id: UUID,
    val iban: String,
    val currency: Currency,
    val customerId: UUID
)
