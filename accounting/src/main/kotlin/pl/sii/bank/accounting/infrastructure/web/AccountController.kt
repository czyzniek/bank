package pl.sii.bank.accounting.infrastructure.web

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pl.sii.bank.accounting.domain.FetchAccountBalanceUseCase
import java.math.BigDecimal
import java.util.UUID

@RestController
class AccountController(
    private val fetchAccountBalanceUseCase: FetchAccountBalanceUseCase
) {
    @GetMapping(
        path = ["/accounts/{accountId}/balance"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun fetchAccountsBalance(@PathVariable accountId: UUID): Map<String, Any> =
        fetchAccountBalanceUseCase.execute(FetchAccountBalanceUseCase.Input(accountId))?.let { output ->
            mapOf(
                "accountId" to accountId,
                "balance" to MonetaryValue(output.balance.amount, output.balance.currency.name)
            )
        } ?: throw IllegalArgumentException("Could not find account!")

    data class MonetaryValue(
        val amount: BigDecimal,
        val currency: String
    )
}
