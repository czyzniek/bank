package pl.sii.bank.transaction.infrastructure.store

import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import pl.sii.bank.transaction.domain.Currency.*
import java.math.BigDecimal
import java.util.*

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = [
        "bank.provider.url=http://localhost:\${wiremock.server.port}",
        "accounting.url=http://localhost:\${wiremock.server.port}"
    ]
)
@AutoConfigureWireMock(port = 0)
@ImportAutoConfiguration(TestChannelBinderConfiguration::class)
@ExtendWith(SoftAssertionsExtension::class)
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
