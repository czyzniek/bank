package pl.sii.bank.accounting.infrastructure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.sii.bank.accounting.domain.CreateAccountForCustomerUseCase
import pl.sii.bank.accounting.domain.CreateCustomerUserCase
import pl.sii.bank.accounting.domain.CustomerStore
import pl.sii.bank.accounting.domain.EventPublisher
import pl.sii.bank.accounting.domain.ExternalAccountProvider
import pl.sii.bank.accounting.domain.ExternalCustomerProvider
import pl.sii.bank.accounting.domain.FetchAccountBalanceUseCase
import pl.sii.bank.accounting.domain.FetchAllCustomersUseCase
import pl.sii.bank.accounting.infrastructure.external.BankExternalAccountBalanceProvider
import pl.sii.bank.accounting.infrastructure.store.AccountRepository
import pl.sii.bank.accounting.infrastructure.store.InMemoryAccountBalanceStore

@Configuration
class AccountingConfiguration {

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
