package pl.sii.bank.transaction

import io.mockk.every
import io.mockk.mockk
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import pl.sii.bank.transaction.domain.*
import pl.sii.bank.transaction.infrastructure.web.TransactionController
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [TransactionController::class])
@Import(ContractBaseClass.ContractConfiguration::class)
open class ContractBaseClass {
    @Autowired
    private lateinit var testController: TransactionController

    @BeforeEach
    fun setup() {
        RestAssuredMockMvc.standaloneSetup(testController)
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