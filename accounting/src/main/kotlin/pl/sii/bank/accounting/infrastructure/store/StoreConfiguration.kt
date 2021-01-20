package pl.sii.bank.accounting.infrastructure.store

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.sii.bank.accounting.domain.CustomerStore
import java.util.concurrent.ConcurrentHashMap

@Configuration
class StoreConfiguration {

    @Bean
    fun accountRepository(): AccountRepository =
        AccountRepository(ConcurrentHashMap())

    @Bean
    fun customerStore(accountRepository: AccountRepository): CustomerStore =
        InMemoryCustomerStore(ConcurrentHashMap(), accountRepository)
}
