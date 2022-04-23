package pl.sii.bank.transaction.infrastructure.web

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import pl.sii.bank.transaction.domain.AuthorizeTransactionUseCase
import pl.sii.bank.transaction.domain.ConfirmTransactionUseCase
import pl.sii.bank.transaction.domain.Currency
import pl.sii.bank.transaction.domain.FetchTransactionStatusUseCase
import pl.sii.bank.transaction.domain.InitializeTransactionUseCase
import pl.sii.bank.transaction.domain.MonetaryValue
import java.util.UUID

@RestController
class TransactionController(
    private val initializeTransactionUseCase: InitializeTransactionUseCase,
    private val confirmTransactionUseCase: ConfirmTransactionUseCase,
    private val authorizeTransactionUseCase: AuthorizeTransactionUseCase,
    private val fetchTransactionStatusUseCase: FetchTransactionStatusUseCase
) {

    @PostMapping(
        path = ["/transactions"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun initializeTransaction(@RequestBody request: InitializeTransactionRequest): Map<String, UUID> {
        val input = InitializeTransactionUseCase.Input(
            request.type,
            request.targetAccount,
            MonetaryValue(request.money.amount, Currency.valueOf(request.money.currency)),
            request.note
        )
        val output = initializeTransactionUseCase.execute(input)
        return mapOf("transactionId" to output.transactionId)
    }

    @PostMapping(
        path = ["/transactions/{transactionId}/confirm"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun confirmTransaction(
        @PathVariable("transactionId") transactionId: UUID,
        @RequestBody request: ConfirmTransactionRequest
    ) {
        val output =
            confirmTransactionUseCase.execute(ConfirmTransactionUseCase.Input(transactionId, request.sourceAccount))
        if (!output.success)
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "Could not confirm transaction!")
    }

    @PostMapping("/transactions/{transactionId}/authorize")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun authorizeTransaction(@PathVariable("transactionId") transactionId: UUID) {
        val output = authorizeTransactionUseCase.execute(AuthorizeTransactionUseCase.Input(transactionId))
        if (!output.success)
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "Could not authorize transaction!")
    }

    @GetMapping(
        path = ["/transactions/{transactionId}/status"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun fetchTransactionStatus(@PathVariable("transactionId") transactionId: UUID): Map<String, String> {
        val output = fetchTransactionStatusUseCase.execute(FetchTransactionStatusUseCase.Input(transactionId))
            ?: throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "Could not authorize transaction!")
        return mapOf("status" to output.status.name)
    }
}
