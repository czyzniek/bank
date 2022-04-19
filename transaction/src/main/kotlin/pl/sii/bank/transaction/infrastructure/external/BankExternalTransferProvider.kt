package pl.sii.bank.transaction.infrastructure.external

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import pl.sii.bank.transaction.domain.ExternalTransferProvider
import java.math.BigDecimal
import java.util.UUID

class BankExternalTransferProvider(
    private val restTemplate: RestTemplate,
    private val log: Logger = LoggerFactory.getLogger(BankExternalTransferProvider::class.java)
) : ExternalTransferProvider {
    override fun submitTransfer(params: ExternalTransferProvider.SubmitTransferParams): ExternalTransferProvider.Result {
        log.info("Submitting transfer to bank provider")
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_XML
        val httpEntity =
            HttpEntity<BankProviderTransferRequest>(BankProviderTransferRequest.fromProviderParams(params), headers)
        val response = restTemplate.postForObject<BankProviderTransferResponse>(
            "/api/transfers",
            httpEntity,
            BankProviderTransferResponse::class
        )
        log.info("Transfer submitted {}", response.id)
        return ExternalTransferProvider.Result(response.id)
    }

    @JacksonXmlRootElement(localName = "transfer")
    data class BankProviderTransferRequest(
        val from: String,
        val to: String,
        val amount: BigDecimal,
        val currency: String,
        val note: String?
    ) {
        companion object Factory {
            fun fromProviderParams(params: ExternalTransferProvider.SubmitTransferParams): BankProviderTransferRequest =
                BankProviderTransferRequest(
                    params.fromAccount,
                    params.toAccount,
                    params.amount,
                    params.currency,
                    params.note
                )
        }
    }

    @JacksonXmlRootElement(localName = "transfer")
    data class BankProviderTransferResponse(val id: UUID)
}
