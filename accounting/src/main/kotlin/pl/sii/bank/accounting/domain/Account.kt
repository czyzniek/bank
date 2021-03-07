package pl.sii.bank.accounting.domain

import pl.sii.bank.accounting.infrastructure.store.AccountEntity
import java.util.*

class Account(
    val id: UUID,
    val iban: String,
    val currency: Currency
) {

    companion object Factory {
        fun new(currency: Currency): Account =
            Account(
                UUID.randomUUID(),
                "PL05103010587984495198645567",
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

