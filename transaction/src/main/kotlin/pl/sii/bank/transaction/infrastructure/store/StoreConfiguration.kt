package pl.sii.bank.transaction.infrastructure.store

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import pl.sii.bank.transaction.domain.AccountBalanceStore
import pl.sii.bank.transaction.domain.CustomerAccountStore
import pl.sii.bank.transaction.domain.TransactionStore
import java.util.concurrent.ConcurrentHashMap

@Configuration
class StoreConfiguration {

    @Bean
    fun transactionStore(): TransactionStore = InMemoryTransactionStore(ConcurrentHashMap())

    @Bean("accountingRestTemplate")
    fun accountingRestTemplate(
        restTemplateBuilder: RestTemplateBuilder,
        @Value("\${accounting.url}") accountingUrl: String,
    ): RestTemplate =
        restTemplateBuilder
            .rootUri(accountingUrl)
            .build()

    @Bean
    fun restAccountBalanceStore(@Qualifier("accountingRestTemplate") restTemplate: RestTemplate): AccountBalanceStore =
        RestAccountBalanceStore(restTemplate)

    @Bean
    fun customerAccountStore(): CustomerAccountStore = InMemoryCustomerAccountStore(ConcurrentHashMap())
}
