package pl.sii.bank.transaction.infrastructure.store

import org.assertj.core.api.Condition
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import pl.sii.bank.transaction.domain.Currency
import pl.sii.bank.transaction.domain.Currency.*
import java.math.BigDecimal
import java.util.*

@ExtendWith(SoftAssertionsExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
    ids = ["pl.sii.bank:accounting:+:stubs:8081"],
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
class RestAccountBalanceStoreTest {

    @Autowired
    private lateinit var accountBalanceStore: RestAccountBalanceStore

    @Test
    fun `should fetch account balance`(softly: SoftAssertions) {
        //given
        val accountId = UUID.randomUUID()

        //when
        val result = accountBalanceStore.fetchAccountBalance(accountId)

        //then
        softly.assertThat(result.accountId).isEqualTo(accountId)
        softly.assertThat(result.balance.amount).isGreaterThan(BigDecimal.ZERO)
        softly.assertThat(result.balance.currency).isIn(EUR, PLN, USD)
    }
}
