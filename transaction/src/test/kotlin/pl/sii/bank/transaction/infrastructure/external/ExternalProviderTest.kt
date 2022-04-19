package pl.sii.bank.transaction.infrastructure.external

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import pl.sii.bank.transaction.domain.ExternalTransferProvider
import java.math.BigDecimal

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = [
        "bank.provider.url=http://localhost:\${wiremock.server.port}",
        "accounting.url=http://localhost:\${wiremock.server.port}"
    ]
)
@AutoConfigureWireMock(port = 0)
@ImportAutoConfiguration(TestChannelBinderConfiguration::class)
class ExternalProviderTest {

    @Autowired
    private lateinit var externalTransferProvider: BankExternalTransferProvider

    @Test
    fun `should submit transfer to external provider`() {
        // given
        val params = ExternalTransferProvider.SubmitTransferParams(
            "PL78816910167843403528847785",
            "PL81124059186726861762149183",
            BigDecimal("1000.23"),
            "PLN",
            null
        )

        // when
        val result = externalTransferProvider.submitTransfer(params)

        // then
        assertThat(result.externalId).isNotNull()
    }
}
