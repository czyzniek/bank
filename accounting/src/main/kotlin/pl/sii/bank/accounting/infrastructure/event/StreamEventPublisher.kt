package pl.sii.bank.accounting.infrastructure.event

import org.springframework.cloud.stream.messaging.Processor
import org.springframework.messaging.support.MessageBuilder
import pl.sii.bank.accounting.domain.EventPublisher

class StreamEventPublisher(private val processor: Processor): EventPublisher {

    override fun sendAccountCreatedEvent(event: EventPublisher.AccountCreated) {
        processor.output().send(MessageBuilder.withPayload(event).build())
    }
}
