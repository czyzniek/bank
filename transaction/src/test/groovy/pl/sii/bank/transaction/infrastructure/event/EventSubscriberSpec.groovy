package pl.sii.bank.transaction.infrastructure.event

import io.github.joke.spockmockable.Mockable
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.StubTrigger
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import pl.sii.bank.transaction.TransactionApplication
import pl.sii.bank.transaction.domain.SaveCustomerAccountUseCase
import spock.lang.Specification

@SpringBootTest(classes = TransactionApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "pl.sii.bank:accounting")
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
@Mockable([SaveCustomerAccountUseCase.class])
class EventSubscriberSpec extends Specification {

    @SpringBean
    SaveCustomerAccountUseCase saveCustomerAccountUseCase = Mock()

    @Autowired
    StubTrigger stubTrigger

    def "should handle new account creation event"() {
        when:
        stubTrigger.trigger('emit_created_account_event')

        then:
        1 * saveCustomerAccountUseCase.execute(_ as SaveCustomerAccountUseCase.Input)
    }
}
