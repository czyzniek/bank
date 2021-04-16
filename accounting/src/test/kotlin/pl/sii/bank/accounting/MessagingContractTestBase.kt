package pl.sii.bank.accounting

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.test.context.junit.jupiter.SpringExtension
import pl.sii.bank.accounting.domain.*
import pl.sii.bank.accounting.domain.Currency
import pl.sii.bank.accounting.infrastructure.web.AccountController
import pl.sii.bank.accounting.infrastructure.web.CustomerController
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

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
