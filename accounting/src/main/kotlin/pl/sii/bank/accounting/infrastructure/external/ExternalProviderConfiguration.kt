package pl.sii.bank.accounting.infrastructure.external

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import pl.sii.bank.accounting.domain.ExternalAccountProvider
import pl.sii.bank.accounting.domain.ExternalCustomerProvider

@Configuration
class ExternalProviderConfiguration(
    @Value("\${bank.provider.url}") private val bankProviderUrl: String
) {

    @Bean("bankExternalRestTemplate")
    fun bankExternalRestTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate =
        restTemplateBuilder.rootUri(bankProviderUrl).build()

    @Bean
    fun bankExternalCustomerProvider(restTemplate: RestTemplate): ExternalCustomerProvider =
        BankExternalCustomerProvider(restTemplate)

    @Bean
    fun bankExternalAccountProvider(restTemplate: RestTemplate): ExternalAccountProvider =
        BankExternalAccountProvider(restTemplate)

    @Bean
    fun bankExternalAccountBalanceProvider(restTemplate: RestTemplate): BankExternalAccountBalanceProvider =
        BankExternalAccountBalanceProvider(restTemplate)
}
