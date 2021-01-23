package pl.sii.bank.transaction.infrastructure.store


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import pl.sii.bank.transaction.domain.Currency
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
    ids = ["pl.sii.bank:accounting:+:stubs:8081"],
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
class RestAccountBalanceStoreSpec extends Specification {

    @Autowired
    RestAccountBalanceStore accountBalanceStore

    def "should fetch account balance"() {
        given:
        def accountId = UUID.randomUUID()

        when:
        def result = accountBalanceStore.fetchAccountBalance(accountId)

        then:
        with(result) {
            it.accountId == accountId
            it.balance.amount > 0
            it.balance.currency == Currency.PLN
        }
    }
}
