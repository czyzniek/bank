package pl.sii.bank.transaction.infrastructure.store

import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.util.*

@ExtendWith(value = [SpringExtension::class, SoftAssertionsExtension::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
    ids = ["pl.sii.bank:accounting:+:stubs:8081"],
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
class RestAccountBalanceStoreTest {

    @Test
    fun `should fetch account balance`(
        @Autowired restAccountBalanceStore: RestAccountBalanceStore,
        softly: SoftAssertions
    ) {
        //given
        val accountId = UUID.randomUUID()

        //when
        val accountBalance = restAccountBalanceStore.fetchAccountBalance(accountId)

        //then
        softly.assertThat(accountBalance.accountId).isEqualTo(accountId)
        softly.assertThat(accountBalance.balance).satisfies { balance ->
            softly.assertThat(balance.amount).isGreaterThan(BigDecimal.ZERO)
            softly.assertThat(balance.currency).isNotNull()
        }
    }
}