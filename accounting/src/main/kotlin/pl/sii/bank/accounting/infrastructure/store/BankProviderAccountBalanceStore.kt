package pl.sii.bank.accounting.infrastructure.store

import org.springframework.web.client.RestTemplate
import pl.sii.bank.accounting.domain.AccountBalanceStore
import pl.sii.bank.accounting.domain.Currency
import pl.sii.bank.accounting.domain.Money
import java.math.BigDecimal
import java.util.*

class BankProviderAccountBalanceStore(
    private val accountRepository: AccountRepository,
    private val restTemplate: RestTemplate
) : AccountBalanceStore {

    override fun fetchBalance(accountId: UUID): Money? =
        accountRepository.findById(accountId)?.let { account ->
            restTemplate.getForObject(
                "/api/accounts/{accountIban}/balance",
                BalanceResponse::class.java,
                account.iban
            )?.let { balance -> Money(balance.amount, Currency.valueOf(balance.currency)) }
        }

    data class BalanceResponse(
        val amount: BigDecimal,
        val currency: String
    )
}
