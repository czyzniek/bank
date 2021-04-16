package pl.sii.bank.accounting.infrastructure.external

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import pl.sii.bank.accounting.domain.Currency
import pl.sii.bank.accounting.domain.FetchAccountBalanceUseCase
import java.math.BigDecimal

class BankExternalAccountBalanceProvider(
    private val restTemplate: RestTemplate,
    private val log: Logger = LoggerFactory.getLogger(FetchAccountBalanceUseCase::class.java)
) {
    fun fetchBalance(accountIban: String): BankProviderAccountBalanceResponse {
        log.info("Fetching balance for account in bank provider")
        return restTemplate.getForObject(
            "/api/accounts/$accountIban/balance",
            BankProviderAccountBalanceResponse::class
        )
    }

    @JacksonXmlRootElement(localName = "balance")
    data class BankProviderAccountBalanceResponse(val amount: BigDecimal, val currency: Currency)
}
