package pl.sii.bank.transaction.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.sii.bank.transaction.infrastructure.event.EventSubscriber
import java.util.*

class SaveCustomerAccountUseCase(
    private val customerAccountStore: CustomerAccountStore,
    private val log: Logger = LoggerFactory.getLogger(EventSubscriber::class.java)
) {

    fun execute(input: Input): Output {
        log.info("Saving newly created account for customer {}", input.customerId)
        val customerAccount = input.toDomain()
        customerAccountStore.save(customerAccount)
        return Output(true)
    }

    data class Input(
        val accountId: UUID,
        val iban: String,
        val currency: Currency,
        val customerId: UUID
    ) {
        fun toDomain(): CustomerAccount =
            CustomerAccount(
                accountId,
                iban,
                currency,
                customerId
            )
    }

    data class Output(
        val success: Boolean
    )
}
