package pl.sii.bank.accounting.domain

import pl.sii.bank.accounting.infrastructure.store.AccountEntity
import java.util.*

class Account(
    val id: UUID,
    val iban: String,
    val currency: Currency
) {

    companion object Factory {
        fun create(iban: String, currency: Currency): Account =
            Account(
                UUID.randomUUID(),
                iban,
                currency
            )

        fun create(accountEntity: AccountEntity): Account =
            Account(
                accountEntity.id,
                accountEntity.iban,
                accountEntity.currency
            )
    }
}

