package pl.sii.bank.transaction.infrastructure.event

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.sii.bank.transaction.domain.Currency
import pl.sii.bank.transaction.domain.SaveCustomerAccountUseCase
import java.util.*
import java.util.function.Consumer

class EventSubscriber(
    private val saveCustomerAccountUseCase: SaveCustomerAccountUseCase,
    private val log: Logger = LoggerFactory.getLogger(EventSubscriber::class.java)
) : Consumer<EventSubscriber.AccountCreated> {

    override fun accept(event: AccountCreated) {
        log.info("Received AccountCreated event.")
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
