package pl.sii.bank.transaction

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = [TransactionApplication])
class TransactionApplicationSpec extends Specification {

    def "loading spring context"() {
        expect:
        true
    }
}
