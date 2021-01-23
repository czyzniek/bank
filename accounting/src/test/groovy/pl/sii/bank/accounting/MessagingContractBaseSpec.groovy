package pl.sii.bank.accounting


import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
import pl.sii.bank.accounting.domain.*
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest(
    classes = [AccountingApplication.class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@AutoConfigureMessageVerifier
class MessagingContractBaseSpec extends Specification {

    static final UUID CUSTOMER_ID = UUID.randomUUID()

    @Autowired
    CreateAccountForCustomerUseCase createAccountForCustomerUseCase

    @SpringBean
    CustomerStore customerStore = Stub() {
        save(_ as Customer) >> { Customer customer -> customer }

        findById(_ as UUID) >> { UUID customerId ->
            new Customer(
                CUSTOMER_ID,
                "Zenon",
                "Nowak",
                LocalDate.of(1990, 11, 23),
                [
                    new Account(
                        UUID.randomUUID(),
                        "PL76124043444059417722471077",
                        Currency.PLN
                    )
                ]
            )
        }
    }

    def accountCreatedTriggered() {
        def input = new CreateAccountForCustomerUseCase.Input(CUSTOMER_ID, "EUR")
        createAccountForCustomerUseCase.execute(input)
    }
}
