package pl.sii.bank.accounting.infrastructure.event

import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Processor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.sii.bank.accounting.domain.EventPublisher

@Configuration
@EnableBinding(Processor::class)
class EventConfiguration {

    @Bean
    fun streamEventPublisher(processor: Processor): EventPublisher =
        StreamEventPublisher(processor)
}
