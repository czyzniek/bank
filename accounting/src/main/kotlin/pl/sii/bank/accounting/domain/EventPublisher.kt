package pl.sii.bank.accounting.domain

import java.util.*

interface EventPublisher {

    fun sendAccountCreatedEvent(event: AccountCreated)

    data class AccountCreated(
        val id: UUID,
        val iban: String,
        val currency: Currency,
        val customerId: UUID
    )
}
