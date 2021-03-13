package pl.sii.bank.accounting

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import pl.sii.bank.accounting.domain.*
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest(
    classes = [AccountingApplication.class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@AutoConfigureMessageVerifier
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
abstract class MessagingContractBaseSpec extends Specification {

    static final UUID CUSTOMER_ID = UUID.randomUUID()

    @Autowired
    CreateAccountForCustomerUseCase createAccountForCustomerUseCase

    @SpringBean
    CustomerStore customerStore = Stub() {
        save(_ as Customer) >> { Customer customer -> customer }

        findById(_ as UUID) >> { UUID customerId ->
            new Customer(
                CUSTOMER_ID,
                UUID.randomUUID(),
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

    @SpringBean
    ExternalAccountProvider accountProvider = Stub() {
        create(_ as ExternalAccountProvider.CreateAccountParams) >> { ExternalAccountProvider.CreateAccountParams params ->
            new ExternalAccountProvider.ExternalAccount(
                UUID.randomUUID(),
                "PL59857010120635280879451363",
                params.currency
            )
        }
    }

    def accountCreatedTriggered() {
        def input = new CreateAccountForCustomerUseCase.Input(CUSTOMER_ID, "EUR")
        createAccountForCustomerUseCase.execute(input)
    }
}
