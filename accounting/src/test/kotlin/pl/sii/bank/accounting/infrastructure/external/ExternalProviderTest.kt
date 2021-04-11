package pl.sii.bank.accounting.infrastructure.external

import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.test.context.TestPropertySource
import pl.sii.bank.accounting.domain.ExternalCustomerProvider
import pl.sii.bank.accounting.infrastructure.web.AccountController
import pl.sii.bank.accounting.infrastructure.web.CustomerController
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(ids = ["pl.sii.bank:bank-provider:+:stubs:9090"], stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@TestPropertySource(properties = ["bank.provider.url=http://localhost:9090"])
@ExtendWith(SoftAssertionsExtension::class)
class ExternalProviderTest {

    @Autowired
    private lateinit var accountProvider: BankExternalAccountProvider
    @Autowired
    private lateinit var customerProvider: BankExternalCustomerProvider
    @Autowired
    private lateinit var accountBalanceProvider: BankExternalAccountBalanceProvider

    @Test
    fun `should create customer in external provider`(softly: SoftAssertions) {
        //given
        val params = ExternalCustomerProvider.CreateCustomerParams(
            "Zenon",
            "Nowak",
            LocalDate.of(1990, 11, 23)
        )

        //when
        val response = customerProvider.create(params)

        //then
        softly.assertThat(response.id).isNotNull()
        softly.assertThat(response.firstName).isEqualTo("Zenon")
        softly.assertThat(response.lastName).isEqualTo("Nowak")
        softly.assertThat(response.birthDate).isEqualTo(LocalDate.of(1990, 11, 23))
    }
}
