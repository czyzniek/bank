package pl.sii.bank.accounting

import io.mockk.every
import io.mockk.mockk
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import pl.sii.bank.accounting.domain.Account
import pl.sii.bank.accounting.domain.CreateAccountForCustomerUseCase
import pl.sii.bank.accounting.domain.CreateCustomerUserCase
import pl.sii.bank.accounting.domain.Currency
import pl.sii.bank.accounting.domain.Customer
import pl.sii.bank.accounting.domain.FetchAccountBalanceUseCase
import pl.sii.bank.accounting.domain.FetchAllCustomersUseCase
import pl.sii.bank.accounting.domain.Money
import pl.sii.bank.accounting.infrastructure.web.AccountController
import pl.sii.bank.accounting.infrastructure.web.CustomerController
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@WebMvcTest(controllers = [CustomerController::class, AccountController::class])
@Import(WebContractTestBase.ContractConfiguration::class)
open class WebContractTestBase {

    @Autowired
    private lateinit var customerController: CustomerController

    @Autowired
    private lateinit var accountController: AccountController

    @BeforeEach
    fun setup() {
        RestAssuredMockMvc.standaloneSetup(customerController, accountController)
    }

    @TestConfiguration
    class ContractConfiguration {

        @Bean
        fun createCustomerUseCase(): CreateCustomerUserCase =
            mockk {
                every { execute(any()) } returns CreateCustomerUserCase.Output(UUID.randomUUID())
            }

        @Bean
        fun fetchAllCustomersUseCase(): FetchAllCustomersUseCase =
            mockk {
                every { execute() } returns FetchAllCustomersUseCase.Output(
                    listOf(
                        Customer(
                            UUID.randomUUID(),
                            UUID.randomUUID(),
                            "Zenon",
                            "Nowak",
                            LocalDate.of(1990, 11, 23),
                            mutableListOf(
                                Account(
                                    UUID.randomUUID(),
                                    "PL24160013746810181484023907",
                                    Currency.PLN
                                )
                            )
                        )
                    )
                )
            }

        @Bean
        fun createAccountForCustomerUseCase(): CreateAccountForCustomerUseCase =
            mockk {
                every { execute(any()) } returns CreateAccountForCustomerUseCase.Output(UUID.randomUUID())
            }

        @Bean
        fun fetchAccountBalanceUseCase(): FetchAccountBalanceUseCase =
            mockk {
                every { execute(any()) } returns FetchAccountBalanceUseCase.Output(
                    UUID.randomUUID(),
                    Money(BigDecimal(1000), Currency.PLN)
                )
            }
    }
}
