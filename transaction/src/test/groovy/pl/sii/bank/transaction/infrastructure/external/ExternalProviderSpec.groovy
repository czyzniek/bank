package pl.sii.bank.transaction.infrastructure.external

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.test.context.TestPropertySource
import pl.sii.bank.transaction.domain.ExternalTransferProvider
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(ids = "pl.sii.bank:bank-provider:+:stubs:9090", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@TestPropertySource(properties = ["bank.provider.url=http://localhost:9090"])
class ExternalProviderSpec extends Specification {

    @Autowired
    BankExternalTransferProvider externalTransferProvider

    def "should submit transfer to external provider"() {
        given:
        def params = new ExternalTransferProvider.SubmitTransferParams(
            "PL78816910167843403528847785",
            "PL81124059186726861762149183",
            new BigDecimal("1000.23"),
            "PLN",
            null
        )

        when:
        def result = externalTransferProvider.submitTransfer(params)

        then:
        with(result) {
            result.externalId != null
        }
    }
}
