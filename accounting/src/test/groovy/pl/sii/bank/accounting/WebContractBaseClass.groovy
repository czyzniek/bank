package pl.sii.bank.accounting

import io.github.joke.spockmockable.Mockable
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import pl.sii.bank.accounting.domain.*
import pl.sii.bank.accounting.infrastructure.web.AccountController
import pl.sii.bank.accounting.infrastructure.web.CustomerController
import spock.lang.Specification

import java.time.LocalDate

@WebMvcTest(controllers = [CustomerController.class, AccountController.class])
@Mockable([CreateCustomerUserCase, FetchAccountBalanceUseCase, FetchAllCustomersUseCase, CreateAccountForCustomerUseCase])
abstract class WebContractBaseClass extends Specification {

    @Autowired
    CustomerController customerController

    @Autowired
    AccountController accountController

    @SpringBean
    CreateCustomerUserCase createCustomerUserCase = Mock() {
        execute(_ as CreateCustomerUserCase.Input) >>
            new CreateCustomerUserCase.Output(UUID.randomUUID())
    }

    @SpringBean
    FetchAllCustomersUseCase fetchAllCustomersUseCase = Mock() {
        execute() >>
            new FetchAllCustomersUseCase.Output([
                new Customer(
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
            ])
    }

    @SpringBean
    FetchAccountBalanceUseCase fetchAccountBalanceUseCase = Mock() {
        execute(_ as FetchAccountBalanceUseCase.Input) >>
            new FetchAccountBalanceUseCase.Output(
                UUID.fromString("4b5dbd29-865e-4781-be21-a031a6a7cdb0"),
                new Money(new BigDecimal(1000), Currency.PLN))
    }

    @SpringBean
    CreateAccountForCustomerUseCase createAccountForCustomerUseCase = Mock() {
        execute(_ as CreateAccountForCustomerUseCase.Input) >>
            new CreateAccountForCustomerUseCase.Output(UUID.randomUUID())
    }

    def setup() {
        RestAssuredMockMvc.standaloneSetup(customerController, accountController)
    }
}
