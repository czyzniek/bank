package pl.sii.bank.transaction.infrastructure.store

import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.core.io.Resource
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.web.client.RestTemplate
import pl.sii.bank.transaction.domain.Currency.*
import java.math.BigDecimal
import java.util.*

@ExtendWith(SoftAssertionsExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RestAccountBalanceStoreMockServerTest(
    @Value("classpath:accounting/fetchAccountBalanceResponse.json") private val fetchAccountBalanceResponse: Resource
) {
    private lateinit var mockServer: MockRestServiceServer

    @BeforeEach
    fun init(@Autowired @Qualifier("accountingRestTemplate") restTemplate: RestTemplate) {
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    @Test
    fun `should fetch account balance`(
        @Autowired accountBalanceStore: RestAccountBalanceStore,
        @Value("\${accounting.url}") accountingUrl: String,
        softly: SoftAssertions
    ) {
        //given
        val accountId = UUID.fromString("2aee4899-2cfa-44ed-a566-d6053e5097c0")

        //and
        mockServer.expect(requestTo("$accountingUrl/accounts/$accountId/balance"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(
                MockRestResponseCreators.withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fetchAccountBalanceResponse))

        //when
        val result = accountBalanceStore.fetchAccountBalance(accountId)

        //then
        softly.assertThat(result.accountId).isEqualTo(accountId)
        softly.assertThat(result.balance.amount).isGreaterThan(BigDecimal.ZERO)
        softly.assertThat(result.balance.currency).isIn(EUR, PLN, USD)
    }
}
