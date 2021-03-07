package pl.sii.bank.accounting.infrastructure

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.sii.bank.accounting.domain.*
import pl.sii.bank.accounting.infrastructure.store.AccountRepository
import pl.sii.bank.accounting.infrastructure.store.BankProviderAccountBalanceStore

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
        eventPublisher: EventPublisher
    ) =
        CreateAccountForCustomerUseCase(customerStore, eventPublisher)

    @Bean
    fun fetchAccountBalanceUseCase(
        accountRepository: AccountRepository,
        restTemplateBuilder: RestTemplateBuilder
    ): FetchAccountBalanceUseCase {
        val accountBalanceStore = BankProviderAccountBalanceStore(
            accountRepository,
            restTemplateBuilder.rootUri(bankProviderUrl).build()
        )
        return FetchAccountBalanceUseCase(accountBalanceStore)
    }
}
