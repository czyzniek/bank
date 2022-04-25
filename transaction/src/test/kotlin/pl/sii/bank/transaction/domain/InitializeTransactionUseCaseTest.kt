package pl.sii.bank.transaction.domain

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import pl.sii.bank.transaction.infrastructure.TransactionConfiguration
import java.math.BigDecimal
import java.util.UUID

internal class InitializeTransactionUseCaseTest {

    private val configuration: TransactionConfiguration = TransactionConfiguration()
    private val mockedTransactionStore: TransactionStore = mock()
    private val subject = configuration.initializeTransactionUseCase(mockedTransactionStore)

    @Test
    fun `should initialize transaction`() {
        //given
        val input = InitializeTransactionUseCase.Input(
            TransactionType.STANDARD, UUID.randomUUID(), MonetaryValue(BigDecimal("50"), Currency.PLN), "Za piwo"
        )

        //and

        `when`(mockedTransactionStore.save(any())).then(returnsFirstArg<Transaction>())

        //when
        val result = subject.execute(input)

        //then
        assertNotNull(result.transactionId)
    }
}

