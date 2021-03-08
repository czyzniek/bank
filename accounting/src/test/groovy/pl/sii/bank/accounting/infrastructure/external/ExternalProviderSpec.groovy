package pl.sii.bank.accounting.infrastructure.external

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.test.context.TestPropertySource
import pl.sii.bank.accounting.domain.Currency
import pl.sii.bank.accounting.domain.ExternalAccountProvider
import pl.sii.bank.accounting.domain.ExternalCustomerProvider
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(ids = "pl.sii.bank:bank-provider:+:stubs:9090", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@TestPropertySource(properties = ["bank.provider.url=http://localhost:9090"])
class ExternalProviderSpec extends Specification {

    @Autowired
    BankExternalAccountProvider accountProvider

    @Autowired
    BankExternalCustomerProvider customerProvider

    @Autowired
    BankExternalAccountBalanceProvider accountBalanceProvider

    def "should create customer in external provier"() {
        given:
        def params = new ExternalCustomerProvider.CreateCustomerParams(
            "Zenon",
            "Nowak",
            LocalDate.of(1990, 11, 23)
        )

        when:
        def response = customerProvider.create(params)

        then:
        with(response) {
            id != null
            firstName == "Zenon"
            lastName == "Nowak"
            birthDate == LocalDate.of(1990, 11, 23)
        }
    }

    def "should create account in external provider"() {
        given:
        def params = new ExternalAccountProvider.CreateAccountParams(
            UUID.randomUUID(),
            Currency.PLN
        )

        when:
        def response = accountProvider.create(params)

        then:
        with(response) {
            id != null
            iban == "PL71147010414407315781808829"
            currency == Currency.PLN
        }
    }

    def "should fetch account balance"() {
        when:
        def response = accountBalanceProvider.fetchBalance("PL68124058468577000440874523")

        then:
        with(response) {
            amount == new BigDecimal("12345.67")
            currency == Currency.PLN
        }
    }
}
