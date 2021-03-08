package pl.sii.bank.accounting.infrastructure.external

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import pl.sii.bank.accounting.domain.Currency
import java.math.BigDecimal

class BankExternalAccountBalanceProvider(private val restTemplate: RestTemplate) {
    fun fetchBalance(accountIban: String): BankProviderAccountBalanceResponse =
        restTemplate.getForObject(
            "/api/accounts/$accountIban/balance",
            BankProviderAccountBalanceResponse::class
        )

    @JacksonXmlRootElement(localName = "balance")
    data class BankProviderAccountBalanceResponse(val amount: BigDecimal, val currency: Currency)
}
