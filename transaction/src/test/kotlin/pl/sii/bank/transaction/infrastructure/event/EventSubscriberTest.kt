package pl.sii.bank.transaction.infrastructure.event

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cloud.contract.stubrunner.StubTrigger
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import pl.sii.bank.transaction.TransactionApplication
import pl.sii.bank.transaction.domain.SaveCustomerAccountUseCase

@SpringBootTest(classes = [TransactionApplication::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = ["pl.sii.bank:accounting"])
@ImportAutoConfiguration(TestChannelBinderConfiguration::class)
@Import(EventSubscriberTest.Configuration::class)
class EventSubscriberTest {

    @Autowired
    private lateinit var saveCustomerAccountUseCase: SaveCustomerAccountUseCase

    @Autowired
    private lateinit var stubTrigger: StubTrigger

    @Test
    fun `should handle new account creation event`() {
        //when
        stubTrigger.trigger("emit_created_account_event")

        //then
        verify { saveCustomerAccountUseCase.execute(any()) }
    }

    @TestConfiguration
    class Configuration {

        @Bean
        @Primary
        fun testSaveCustomerAccountUseCase(): SaveCustomerAccountUseCase =
            mockk {
                every { execute(any()) } returns SaveCustomerAccountUseCase.Output(true)
            }
    }
}
