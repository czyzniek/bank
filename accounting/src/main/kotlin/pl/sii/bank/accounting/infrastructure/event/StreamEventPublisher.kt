package pl.sii.bank.accounting.infrastructure.event

import org.springframework.cloud.stream.function.StreamBridge
import pl.sii.bank.accounting.domain.EventPublisher

class StreamEventPublisher(private val streamBridge: StreamBridge) : EventPublisher {

    override fun sendAccountCreatedEvent(event: EventPublisher.AccountCreated) {
        streamBridge.send("accountCreated-out-0", event)
    }
}
