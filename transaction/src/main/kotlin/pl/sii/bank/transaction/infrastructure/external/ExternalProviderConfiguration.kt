package pl.sii.bank.transaction.infrastructure.external

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.sii.bank.transaction.domain.ExternalTransferProvider

@Configuration
class ExternalProviderConfiguration(
    @Value("\${bank.provider.url}") private val bankProviderUrl: String
) {

    @Bean
    fun bankExternalTransferProvider(restTemplateBuilder: RestTemplateBuilder): ExternalTransferProvider =
        BankExternalTransferProvider(restTemplateBuilder.rootUri(bankProviderUrl).build())
}
