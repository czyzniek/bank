package pl.sii.bank.transaction

import io.mockk.every
import io.mockk.mockk
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import pl.sii.bank.transaction.domain.AuthorizeTransactionUseCase
import pl.sii.bank.transaction.domain.ConfirmTransactionUseCase
import pl.sii.bank.transaction.domain.FetchTransactionStatusUseCase
import pl.sii.bank.transaction.domain.InitializeTransactionUseCase
import pl.sii.bank.transaction.domain.TransactionStatus
import pl.sii.bank.transaction.infrastructure.web.TransactionController
import java.util.UUID

@WebMvcTest(controllers = [TransactionController::class])
@Import(WebContractTestBase.ContractConfiguration::class)
open class WebContractTestBase {

    @Autowired
    private lateinit var transactionController: TransactionController

    @BeforeEach
    fun setup() {
        RestAssuredMockMvc.standaloneSetup(transactionController)
    }

    @TestConfiguration
    class ContractConfiguration {

        @Bean
        fun initializeTransactionUseCase(): InitializeTransactionUseCase =
            mockk() {
                every { execute(any()) } returns InitializeTransactionUseCase.Output(UUID.randomUUID())
            }

        @Bean
        fun confirmTransactionUseCase(): ConfirmTransactionUseCase =
            mockk() {
                every { execute(any()) } returns ConfirmTransactionUseCase.Output(true)
            }

        @Bean
        fun authorizeTransactionUseCase(): AuthorizeTransactionUseCase =
            mockk() {
                every { execute(any()) } returns AuthorizeTransactionUseCase.Output(true)
            }

        @Bean
        fun fetchTransactionStatusUseCase(): FetchTransactionStatusUseCase =
            mockk() {
                every { execute(any()) } returns FetchTransactionStatusUseCase.Output(TransactionStatus.INITIALIZED)
            }
    }
}
