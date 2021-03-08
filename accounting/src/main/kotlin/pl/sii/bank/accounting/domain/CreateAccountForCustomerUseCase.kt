package pl.sii.bank.accounting.domain

import java.util.*

class CreateAccountForCustomerUseCase(
    private val customerStore: CustomerStore,
    private val externalAccountProvider: ExternalAccountProvider,
    private val eventPublisher: EventPublisher
) {
    fun execute(input: Input): Output? =
        customerStore.findById(input.customerId)?.let { foundCustomer ->
            val params = ExternalAccountProvider.CreateAccountParams(
                foundCustomer.externalId, Currency.valueOf(input.currency)
            )
            val externalAccount = externalAccountProvider.create(params)
            val account = Account.create(externalAccount.iban, externalAccount.currency)
            foundCustomer.accounts.add(account)
            customerStore.save(foundCustomer)
            eventPublisher.sendAccountCreatedEvent(
                EventPublisher.AccountCreated(account.id, account.iban, account.currency, input.customerId)
            )
            Output(account.id)
        }

    data class Input(
        val customerId: UUID,
        val currency: String
    )

    data class Output(
        val accountId: UUID
    )
}
