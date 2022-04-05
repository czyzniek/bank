package pl.sii.bank.accounting.infrastructure.external

import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import pl.sii.bank.accounting.domain.ExternalCustomerProvider
import java.time.LocalDate


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = ["bank.provider.url=http://localhost:\${wiremock.server.port}"]
)
@AutoConfigureWireMock(port = 0)
@ImportAutoConfiguration(TestChannelBinderConfiguration::class)
@ExtendWith(SoftAssertionsExtension::class)
class ExternalProviderTest() {

    @Test
    fun `should create customer in external provider`(
        @Autowired customerProvider: BankExternalCustomerProvider,
        softly: SoftAssertions
    ) {
        //given
        val params = ExternalCustomerProvider.CreateCustomerParams(
            "Zenon",
            "Nowak",
            LocalDate.of(1990, 11, 23)
        )

        //when
        val response = customerProvider.create(params)

        //then
        softly.assertThat(response.id).isNotNull
        softly.assertThat(response.firstName).isEqualTo("Zenon")
        softly.assertThat(response.lastName).isEqualTo("Nowak")
        softly.assertThat(response.birthDate).isEqualTo(LocalDate.of(1990, 11, 23))
    }
}
