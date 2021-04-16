package pl.sii.bank.transaction.infrastructure.store

import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.test.context.TestPropertySource
import pl.sii.bank.transaction.domain.Currency.*
import java.math.BigDecimal
import java.util.*

@ExtendWith(SoftAssertionsExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
    ids = ["pl.sii.bank:accounting:+:stubs:9090"],
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@TestPropertySource(properties = ["accounting.url=http://localhost:9090"])
class RestAccountBalanceStoreTest {

    @Test
    fun `should fetch account balance`(
        @Autowired accountBalanceStore: RestAccountBalanceStore,
        softly: SoftAssertions
    ) {
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
