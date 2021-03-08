package pl.sii.bank.accounting.infrastructure.external

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import pl.sii.bank.accounting.domain.Currency
import pl.sii.bank.accounting.domain.ExternalAccountProvider
import java.util.*

class BankExternalAccountProvider(private val restTemplate: RestTemplate) : ExternalAccountProvider {
    override fun create(params: ExternalAccountProvider.CreateAccountParams): ExternalAccountProvider.ExternalAccount {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_XML
        val httpEntity =
            HttpEntity<BankProviderAccountRequest>(BankProviderAccountRequest.fromProviderParams(params), headers)
        val response = restTemplate.postForObject<BankProviderAccountResponse>(
            "/api/customers/${params.customerId}/accounts",
            httpEntity,
            BankProviderAccountResponse::class
        )
        return ExternalAccountProvider.ExternalAccount(response.id, response.iban, response.currency)
    }

    @JacksonXmlRootElement(localName = "account")
    data class BankProviderAccountRequest(
        val currency: Currency
    ) {
        companion object Factory {
            fun fromProviderParams(params: ExternalAccountProvider.CreateAccountParams): BankProviderAccountRequest =
                BankProviderAccountRequest(params.currency)
        }
    }

    @JacksonXmlRootElement(localName = "account")
    data class BankProviderAccountResponse(val id: UUID, val iban: String, val currency: Currency)
}
