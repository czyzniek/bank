package pl.sii.bank.transaction.infrastructure.store

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.sii.bank.transaction.domain.AccountBalanceStore
import pl.sii.bank.transaction.domain.CustomerAccountStore
import pl.sii.bank.transaction.domain.TransactionStore
import java.util.concurrent.ConcurrentHashMap

@Configuration
class StoreConfiguration {

    @Bean
    fun transactionStore(): TransactionStore = InMemoryTransactionStore(ConcurrentHashMap())

    @Bean
    fun restAccountBalanceStore(restTemplateBuilder: RestTemplateBuilder): AccountBalanceStore =
        RestAccountBalanceStore(
            restTemplateBuilder
                .rootUri("http://localhost:8081")
                .build()
        )

    @Bean
    fun customerAccountStore(): CustomerAccountStore = InMemoryCustomerAccountStore(ConcurrentHashMap())
}
