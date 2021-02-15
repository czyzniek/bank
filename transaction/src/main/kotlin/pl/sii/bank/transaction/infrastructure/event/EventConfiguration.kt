package pl.sii.bank.transaction.infrastructure.event

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.sii.bank.transaction.domain.SaveCustomerAccountUseCase

@Configuration
class EventConfiguration {

    @Bean("accountCreated")
    fun saveCustomerAccountSubscriber(saveCustomerAccountUseCase: SaveCustomerAccountUseCase): EventSubscriber =
        EventSubscriber(saveCustomerAccountUseCase)
}
