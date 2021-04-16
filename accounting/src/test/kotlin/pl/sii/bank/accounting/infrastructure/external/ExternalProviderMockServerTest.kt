package pl.sii.bank.accounting.infrastructure.external

import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.*
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.web.client.RestTemplate
import pl.sii.bank.accounting.domain.ExternalCustomerProvider
import java.time.LocalDate


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SoftAssertionsExtension::class)
class ExternalProviderMockServerTest(
    @Value("\${bank.provider.url}") private val bankProviderUrl: String,
    @Value("classpath:external/createCustomerResponse.xml") private val createCustomerResponse: Resource
) {

    private lateinit var mockServer: MockRestServiceServer

    @BeforeEach
    fun init(@Autowired @Qualifier("bankExternalRestTemplate") restTemplate: RestTemplate) {
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    @Test
    fun `should create customer in external provider`(
        @Autowired customerProvider: BankExternalCustomerProvider,
        softly: SoftAssertions
    ) {
        //given
        mockServer.expect(requestTo("$bankProviderUrl/api/customers"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().contentType(MediaType.APPLICATION_XML))
            .andExpect(content().xml("""
                <customer>
                    <firstName>Zenon</firstName>
                    <lastName>Nowak</lastName>
                    <birthDate>1990-11-23</birthDate>
                </customer>
            """.trimIndent()))
            .andRespond(withStatus(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_XML)
                .body(createCustomerResponse))

        //and
        val params = ExternalCustomerProvider.CreateCustomerParams(
            "Zenon",
            "Nowak",
            LocalDate.of(1990, 11, 23)
        )

        //when
        val response = customerProvider.create(params)

        //then
        softly.assertThat(response.id).isNotNull()
        softly.assertThat(response.firstName).isEqualTo("Zenon")
        softly.assertThat(response.lastName).isEqualTo("Nowak")
        softly.assertThat(response.birthDate).isEqualTo(LocalDate.of(1990, 11, 23))
    }
}
