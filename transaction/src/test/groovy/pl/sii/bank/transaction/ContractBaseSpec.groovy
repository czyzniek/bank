package pl.sii.bank.transaction

import io.github.joke.spockmockable.Mockable
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import pl.sii.bank.transaction.domain.*
import pl.sii.bank.transaction.infrastructure.web.TransactionController
import spock.lang.Specification

@WebMvcTest(controllers = [TransactionController])
@Mockable([InitializeTransactionUseCase, ConfirmTransactionUseCase, AuthorizeTransactionUseCase, FetchTransactionStatusUseCase])
abstract class ContractBaseSpec extends Specification {
    @Autowired
    TransactionController testController

    @SpringBean
    InitializeTransactionUseCase initializeTransactionUseCase = Mock() {
        execute(_ as InitializeTransactionUseCase.Input) >>
            new InitializeTransactionUseCase.Output(UUID.randomUUID())
    }

    @SpringBean
    ConfirmTransactionUseCase confirmTransactionUseCase = Mock() {
        execute(_ as ConfirmTransactionUseCase.Input) >>
            new ConfirmTransactionUseCase.Output(true)
    }

    @SpringBean
    AuthorizeTransactionUseCase authorizeTransactionUseCase = Mock() {
        execute(_ as AuthorizeTransactionUseCase.Input) >>
            new AuthorizeTransactionUseCase.Output(true)
    }

    @SpringBean
    FetchTransactionStatusUseCase fetchTransactionStatusUseCase = Mock() {
        execute(_ as FetchTransactionStatusUseCase.Input) >>
            new FetchTransactionStatusUseCase.Output(TransactionStatus.INITIALIZED)
    }

    def setup() {
        RestAssuredMockMvc.standaloneSetup(testController)
    }
}
