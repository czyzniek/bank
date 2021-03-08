package pl.sii.bank.accounting.infrastructure

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.sii.bank.accounting.domain.*
import pl.sii.bank.accounting.infrastructure.external.BankExternalAccountBalanceProvider
import pl.sii.bank.accounting.infrastructure.store.AccountRepository
import pl.sii.bank.accounting.infrastructure.store.InMemoryAccountBalanceStore

@Configuration
class AccountingConfiguration(
    @Value("\${bank.provider.url}") private val bankProviderUrl: String
) {

    @Bean
    fun createCustomerUseCase(
        customerStore: CustomerStore,
        externalCustomerProvider: ExternalCustomerProvider
    ) =
        CreateCustomerUserCase(customerStore, externalCustomerProvider)

    @Bean
    fun fetchAllCustomersUseCase(customerStore: CustomerStore) =
        FetchAllCustomersUseCase(customerStore)

    @Bean
    fun createAccountForCustomerUseCase(
        customerStore: CustomerStore,
        externalAccountProvider: ExternalAccountProvider,
        eventPublisher: EventPublisher
    ) =
        CreateAccountForCustomerUseCase(customerStore, externalAccountProvider, eventPublisher)

    @Bean
    fun fetchAccountBalanceUseCase(
        accountRepository: AccountRepository,
        bankExternalAccountBalanceProvider: BankExternalAccountBalanceProvider
    ): FetchAccountBalanceUseCase {
        val accountBalanceStore = InMemoryAccountBalanceStore(
            accountRepository,
            bankExternalAccountBalanceProvider
        )
        return FetchAccountBalanceUseCase(accountBalanceStore)
    }
}
