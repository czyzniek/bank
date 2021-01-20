package pl.sii.bank.accounting

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.context.junit.jupiter.SpringExtension
import pl.sii.bank.accounting.domain.*
import pl.sii.bank.accounting.domain.Currency
import java.time.LocalDate
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    classes = [AccountingApplication::class, MessagingContractBaseClass.ContractConfiguration::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@AutoConfigureMessageVerifier
open class MessagingContractBaseClass {

    companion object {
        val CUSTOMER_ID: UUID = UUID.randomUUID()
    }

    @Autowired
    lateinit var createAccountForCustomerUseCase: CreateAccountForCustomerUseCase

    fun accountCreatedTriggered() {
        val input = CreateAccountForCustomerUseCase.Input(CUSTOMER_ID, "EUR")
        createAccountForCustomerUseCase.execute(input)
    }

    @TestConfiguration
    class ContractConfiguration {

        @Bean
        @Primary
        fun testCustomerStore(): CustomerStore =
            object : CustomerStore {
                override fun save(customer: Customer): Customer = customer

                override fun findAll(): List<Customer> {
                    throw UnsupportedOperationException("Not used in test")
                }

                override fun findById(customerId: UUID): Customer? = Customer(
                    CUSTOMER_ID,
                    "Zenon",
                    "Nowak",
                    LocalDate.of(1990, 11, 23),
                    mutableListOf(
                        Account(
                            UUID.randomUUID(),
                            "IBAN",
                            Currency.PLN
                        )
                    )
                )
            }
    }
}
