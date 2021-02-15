package pl.sii.bank.accounting.infrastructure.event

import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.sii.bank.accounting.domain.EventPublisher

@Configuration
class EventConfiguration {

    @Bean
    fun streamEventPublisher(streamBridge: StreamBridge): EventPublisher =
        StreamEventPublisher(streamBridge)
}
