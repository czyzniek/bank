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
    classes = [AccountingApplication::class, MessagingContractTestBase.ContractConfiguration::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@AutoConfigureMessageVerifier
@ImportAutoConfiguration(TestChannelBinderConfiguration::class)
open class MessagingContractTestBase {

    private val CUSTOMER_ID: UUID = UUID.randomUUID()

    @Autowired
    private lateinit var createAccountForCustomerUseCase: CreateAccountForCustomerUseCase

    fun accountCreatedTriggered() {
        val input = CreateAccountForCustomerUseCase.Input(CUSTOMER_ID, "EUR")
        createAccountForCustomerUseCase.execute(input)
    }

    @TestConfiguration
    class ContractConfiguration {

        val savedCustomer = slot<Customer>()
        val customerId = slot<UUID>()
        val createAccountParams = slot<ExternalAccountProvider.CreateAccountParams>()

        @Bean
        @Primary
        fun testCustomerStore(): CustomerStore =
            mockk {
                every { save(capture(savedCustomer)) } answers { savedCustomer.captured }

                every { findById(capture(customerId)) } answers {
                    Customer(
                        customerId.captured,
                        UUID.randomUUID(),
                        "Zenon",
                        "Nowak",
                        LocalDate.of(1990, 11, 23),
                        mutableListOf(
                            Account(
                                UUID.randomUUID(),
                                "PL76124043444059417722471077",
                                Currency.PLN
                            )
                        )
                    )
                }
            }

        @Bean
        @Primary
        fun testAccountProvider(): ExternalAccountProvider =
            mockk {
                every { create(capture(createAccountParams)) } answers {
                    ExternalAccountProvider.ExternalAccount(
                        UUID.randomUUID(),
                        "PL59857010120635280879451363",
                        createAccountParams.captured.currency
                    )
                }
            }
    }
}
