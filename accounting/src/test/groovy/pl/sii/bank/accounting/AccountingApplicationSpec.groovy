package pl.sii.bank.accounting

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = [AccountingApplication])
class AccountingApplicationSpec extends Specification {

    def "loading spring context"() {
        expect:
        true
    }
}
