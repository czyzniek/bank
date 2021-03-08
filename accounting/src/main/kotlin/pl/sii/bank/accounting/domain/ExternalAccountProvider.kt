package pl.sii.bank.accounting.domain

import java.util.*

interface ExternalAccountProvider {

    fun create(params: CreateAccountParams): ExternalAccount

    data class CreateAccountParams(
        val customerId: UUID,
        val currency: Currency
    )

    data class ExternalAccount(
        val id: UUID,
        val iban: String,
        val currency: Currency
    )
}
