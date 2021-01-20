package pl.sii.bank.accounting

import io.github.joke.spockmockable.Mockable
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import pl.sii.bank.accounting.domain.CreateAccountForCustomerUseCase
import pl.sii.bank.accounting.domain.CreateCustomerUserCase
import pl.sii.bank.accounting.domain.FetchAccountBalanceUseCase
import pl.sii.bank.accounting.domain.FetchAllCustomersUseCase
import pl.sii.bank.accounting.infrastructure.web.AccountController
import pl.sii.bank.accounting.infrastructure.web.CustomerController
import spock.lang.Specification
import spock.mock.DetachedMockFactory

@WebMvcTest(controllers = [CustomerController.class, AccountController.class])
@Import(ContractConfiguration)
@Mockable([CreateCustomerUserCase, FetchAccountBalanceUseCase, FetchAllCustomersUseCase, CreateAccountForCustomerUseCase])
abstract class SpockWebContractBaseClass extends Specification {

    @Autowired
    CustomerController customerController

    @Autowired
    AccountController accountController

    @Autowired
    FetchAccountBalanceUseCase fetchAccountBalanceUseCase

    def setup() {
        RestAssuredMockMvc.standaloneSetup(customerController, accountController)
    }

    @TestConfiguration
    static class ContractConfiguration {
        def mockFactory = new DetachedMockFactory()

        @Bean
        CreateCustomerUserCase createCustomerUserCase() {
            def mock = mockFactory.Mock(CreateCustomerUserCase)
            return mock
        }

        @Bean
        FetchAllCustomersUseCase fetchAllCustomersUseCase() {
            return mockFactory.Mock(FetchAllCustomersUseCase)

        }

        @Bean
        FetchAccountBalanceUseCase fetchAccountBalanceUseCase() {
            def mock = mockFactory.Mock(FetchAccountBalanceUseCase)
            return mock

        }

        @Bean
        CreateAccountForCustomerUseCase createAccountForCustomerUseCase() {
            return mockFactory.Mock(CreateAccountForCustomerUseCase)
        }
    }
}
