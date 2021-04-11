package pl.sii.bank.transaction.infrastructure.store

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate
import pl.sii.bank.transaction.domain.AccountBalanceStore
import pl.sii.bank.transaction.domain.ConfirmTransactionUseCase
import java.util.*
import kotlin.NoSuchElementException

class RestAccountBalanceStore(
    private val restTemplate: RestTemplate,
    private val log: Logger = LoggerFactory.getLogger(RestAccountBalanceStore::class.java)
) : AccountBalanceStore {
    private val balancePath: String = "/accounts/{accountId}/balance"

    override fun fetchAccountBalance(accountId: UUID): AccountBalanceStore.AccountBalance {
        log.info("Fetching balance for account {}", accountId)
        return restTemplate.getForObject(balancePath, AccountBalanceStore.AccountBalance::class.java, accountId)
            ?: throw NoSuchElementException("Could not find balance for account $accountId")
    }
}
