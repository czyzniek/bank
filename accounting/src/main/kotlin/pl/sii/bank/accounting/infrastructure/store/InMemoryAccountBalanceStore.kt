package pl.sii.bank.accounting.infrastructure.store

import pl.sii.bank.accounting.domain.AccountBalanceStore
import pl.sii.bank.accounting.domain.Money
import pl.sii.bank.accounting.infrastructure.external.BankExternalAccountBalanceProvider
import java.util.UUID

class InMemoryAccountBalanceStore(
    private val accountRepository: AccountRepository,
    private val bankExternalAccountBalanceProvider: BankExternalAccountBalanceProvider
) : AccountBalanceStore {

    override fun fetchBalance(accountId: UUID): Money? =
        accountRepository.findById(accountId)?.let { account ->
            bankExternalAccountBalanceProvider.fetchBalance(account.iban)
                .let { balance -> Money(balance.amount, balance.currency) }
        }
}
