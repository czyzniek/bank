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
import pl.sii.bank.accounting.domain.AccountBalanceStore
import pl.sii.bank.accounting.domain.Currency
import pl.sii.bank.accounting.domain.ExternalAccountProvider
import pl.sii.bank.accounting.domain.ExternalCustomerProvider
import pl.sii.bank.accounting.infrastructure.web.AccountController
import pl.sii.bank.accounting.infrastructure.web.CustomerController
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(
    ids = ["pl.sii.bank:bank-provider:+:stubs:9090"],
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@TestPropertySource(properties = ["bank.provider.url=http://localhost:9090"])
@ExtendWith(SoftAssertionsExtension::class)
class ExternalProviderTest {

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
        softly.assertThat(response.id).isNotNull()
        softly.assertThat(response.firstName).isEqualTo("Zenon")
        softly.assertThat(response.lastName).isEqualTo("Nowak")
        softly.assertThat(response.birthDate).isEqualTo(LocalDate.of(1990, 11, 23))
    }

    @Test
    fun `should create account for customer`(
        @Autowired accountProvider: BankExternalAccountProvider,
        softly: SoftAssertions
    ) {
        //given
        val params = ExternalAccountProvider.CreateAccountParams(
            UUID.randomUUID(),
            Currency.EUR
        )

        //when
        val response = accountProvider.create(params)

        //then
        softly.assertThat(response.id).isNotNull()
        softly.assertThat(response.iban).isEqualTo("PL71147010414407315781808829")
        softly.assertThat(response.currency).isIn(Currency.PLN, Currency.EUR, Currency.USD)
    }

    @Test
    fun `should fetch balance for given account`(
        @Autowired accountBalanceProvider: BankExternalAccountBalanceProvider,
        softly: SoftAssertions
    ) {
        //given
        val accountIban = "PL09175013093816524046671521"

        //when
        val response = accountBalanceProvider.fetchBalance(accountIban)

        //then
        softly.assertThat(response.currency).isIn(Currency.PLN, Currency.EUR, Currency.USD)
        softly.assertThat(response.amount).isEqualTo(BigDecimal("12345.67"))
    }
}
