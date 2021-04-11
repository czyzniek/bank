package pl.sii.bank.accounting.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class CreateAccountForCustomerUseCase(
    private val customerStore: CustomerStore,
    private val externalAccountProvider: ExternalAccountProvider,
    private val eventPublisher: EventPublisher,
    private val log: Logger = LoggerFactory.getLogger(CreateAccountForCustomerUseCase::class.java)
) {
    fun execute(input: Input): Output? {
        log.info("Creating account {} for customer {}", input.currency, input.customerId)
        return customerStore.findById(input.customerId)?.let { foundCustomer ->
            log.info("Creating account in external provider")
            val params = ExternalAccountProvider.CreateAccountParams(
                foundCustomer.externalId, Currency.valueOf(input.currency)
            )
            val externalAccount = externalAccountProvider.create(params)
            val account = Account.create(externalAccount.iban, externalAccount.currency)
            foundCustomer.accounts.add(account)
            log.info("Saving newly created account {}", account.id)
            customerStore.save(foundCustomer)
            log.info("Sending account created domain event")
            eventPublisher.sendAccountCreatedEvent(
                EventPublisher.AccountCreated(account.id, account.iban, account.currency, input.customerId)
            )
            Output(account.id)
        }
    }

    data class Input(
        val customerId: UUID,
        val currency: String
    )

    data class Output(
        val accountId: UUID
    )
}
