package pl.sii.bank.transaction.infrastructure.store

import org.springframework.web.client.RestTemplate
import pl.sii.bank.transaction.domain.AccountBalanceStore
import java.util.*
import kotlin.NoSuchElementException

class RestAccountBalanceStore(private val restTemplate: RestTemplate) : AccountBalanceStore {
    private val balancePath: String = "/accounts/{accountId}/balance"

    override fun fetchAccountBalance(accountId: UUID): AccountBalanceStore.AccountBalance =
        restTemplate.getForObject(balancePath, AccountBalanceStore.AccountBalance::class.java, accountId)
            ?: throw NoSuchElementException("Could not find balance for account $accountId")
}