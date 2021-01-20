package pl.sii.bank.accounting

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification
import io.restassured.response.ResponseOptions
import org.springframework.beans.factory.annotation.Autowired
import pl.sii.bank.accounting.domain.Currency
import pl.sii.bank.accounting.domain.FetchAccountBalanceUseCase
import pl.sii.bank.accounting.domain.Money

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given

class FetchAccountBalanceUseCaseSpec extends SpockWebContractBaseClass {

    @Autowired
    FetchAccountBalanceUseCase fetchAccountBalanceUseCase

    def validate_fetchAccountBalance() throws Exception {
        given:
        MockMvcRequestSpecification request = given()

        when:
        ResponseOptions response = given().spec(request)
            .get("/accounts/4b5dbd29-865e-4781-be21-a031a6a7cdb0/balance")

        then:
        response.statusCode() == 200
        response.header("Content-Type") ==~ java.util.regex.Pattern.compile('application/json.*')

        and:
        DocumentContext parsedJson = JsonPath.parse(response.body.asString())
        assertThatJson(parsedJson).field("['accountId']").isEqualTo("4b5dbd29-865e-4781-be21-a031a6a7cdb0")
        assertThatJson(parsedJson).field("['balance']").field("['amount']").matches("[1-9][0-9]{0,10}|0\\.0[1-9]|0\\.[1-9][0-9]?|[1-9][0-9]{0,10}\\.[0-9]{1,2}")
        assertThatJson(parsedJson).field("['balance']").field("['currency']").isEqualTo("PLN")

        and:
        1 * fetchAccountBalanceUseCase.execute(_ as FetchAccountBalanceUseCase.Input) >>
            new FetchAccountBalanceUseCase.Output(
                UUID.fromString("4b5dbd29-865e-4781-be21-a031a6a7cdb0"),
                new Money(new BigDecimal(1000), Currency.PLN))
    }
}
