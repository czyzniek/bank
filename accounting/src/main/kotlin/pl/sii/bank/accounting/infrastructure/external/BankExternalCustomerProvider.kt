package pl.sii.bank.accounting.infrastructure.external

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import pl.sii.bank.accounting.domain.ExternalCustomerProvider
import java.time.LocalDate
import java.util.*

class BankExternalCustomerProvider(private val restTemplate: RestTemplate) : ExternalCustomerProvider {
    override fun create(params: ExternalCustomerProvider.CreateCustomerParams): ExternalCustomerProvider.ExternalCustomer {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_XML
        val httpEntity =
            HttpEntity<BankProviderCustomerRequest>(BankProviderCustomerRequest.fromProviderParams(params), headers)
        val response = restTemplate.postForObject<BankProviderCustomerResponse>(
            "/api/customers",
            httpEntity,
            BankProviderCustomerResponse::class
        )
        return ExternalCustomerProvider.ExternalCustomer(
            response.id,
            params.firstName,
            params.lastName,
            params.birthDate
        )
    }

    @JacksonXmlRootElement(localName = "customer")
    data class BankProviderCustomerRequest(
        val firstName: String,
        val lastName: String,
        val birthDate: LocalDate
    ) {
        companion object Factory {
            fun fromProviderParams(params: ExternalCustomerProvider.CreateCustomerParams): BankProviderCustomerRequest =
                BankProviderCustomerRequest(params.firstName, params.lastName, params.birthDate)
        }
    }

    @JacksonXmlRootElement(localName = "customer")
    data class BankProviderCustomerResponse(val id: UUID)
}
