package pl.sii.bank.transaction.infrastructure.event

import pl.sii.bank.transaction.domain.Currency
import pl.sii.bank.transaction.domain.SaveCustomerAccountUseCase
import java.util.*
import java.util.function.Consumer

class EventSubscriber(
    private val saveCustomerAccountUseCase: SaveCustomerAccountUseCase
) : Consumer<EventSubscriber.AccountCreated> {

    override fun accept(event: AccountCreated) {
        saveCustomerAccountUseCase.execute(event.toInput())
    }

    data class AccountCreated(
        val id: UUID,
        val iban: String,
        val currency: Currency,
        val customerId: UUID
    ) {
        fun toInput(): SaveCustomerAccountUseCase.Input =
            SaveCustomerAccountUseCase.Input(
                id,
                iban,
                currency,
                customerId
            )
    }
}
