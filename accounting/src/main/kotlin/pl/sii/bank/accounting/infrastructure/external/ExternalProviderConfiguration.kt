package pl.sii.bank.accounting.infrastructure.external

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.sii.bank.accounting.domain.ExternalCustomerProvider

@Configuration
class ExternalProviderConfiguration(
    @Value("\${bank.provider.url}") private val bankProviderUrl: String
) {

    @Bean
    fun bankExternalCustomerProvider(restTemplateBuilder: RestTemplateBuilder): ExternalCustomerProvider =
        BankExternalCustomerProvider(restTemplateBuilder.rootUri(bankProviderUrl).build())
}
