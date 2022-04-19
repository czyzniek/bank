package pl.sii.bank.accounting

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import pl.sii.bank.accounting.domain.Currency
import pl.sii.bank.accounting.domain.EventPublisher
import java.util.UUID

@SpringBootTest(
    classes = [AccountingApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@AutoConfigureMessageVerifier
@ImportAutoConfiguration(TestChannelBinderConfiguration::class)
open class MessagingContractTestBase {

    private val CUSTOMER_ID: UUID = UUID.randomUUID()

    @Autowired
    private lateinit var eventPublisher: EventPublisher

    fun accountCreatedTriggered() {
        val accountCratedEvent = EventPublisher.AccountCreated(
            UUID.randomUUID(),
            "PL76124043444059417722471077",
            Currency.PLN,
            CUSTOMER_ID
        )
        eventPublisher.sendAccountCreatedEvent(accountCratedEvent)
    }
}
