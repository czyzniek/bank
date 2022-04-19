package pl.sii.bank.accounting.infrastructure.web

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.sii.bank.accounting.domain.CreateAccountForCustomerUseCase
import pl.sii.bank.accounting.domain.CreateCustomerUserCase
import pl.sii.bank.accounting.domain.FetchAllCustomersUseCase
import java.util.UUID

@RestController
class CustomerController(
    private val createCustomerUserCase: CreateCustomerUserCase,
    private val fetchAllCustomersUseCase: FetchAllCustomersUseCase,
    private val createAccountForCustomerUseCase: CreateAccountForCustomerUseCase
) {
    @PostMapping(
        path = ["/customers"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@RequestBody request: CreateCustomerRequest): Map<String, UUID> {
        val input = CreateCustomerUserCase.Input(request.firstName, request.lastName, request.birthDate)
        val output = createCustomerUserCase.execute(input)
        return mapOf("customerId" to output.id)
    }

    @PostMapping(
        path = ["/customers/{customerId}/accounts"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccountForCustomer(
        @PathVariable("customerId") customerId: UUID,
        @RequestBody request: CreateAccountForCustomerRequest
    ): Map<String, UUID> {
        val output =
            createAccountForCustomerUseCase.execute(CreateAccountForCustomerUseCase.Input(customerId, request.currency))
                ?: throw IllegalArgumentException("Could not find customer!")
        return mapOf("accountId" to output.accountId)
    }

    @GetMapping(
        path = ["/customers"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun fetchAllCustomers(): Map<String, List<CustomerResponse>> {
        val output = fetchAllCustomersUseCase.execute()
        return mapOf(
            "customers" to output.customers.map { customer ->
                CustomerResponse(
                    customer.id,
                    customer.firstName,
                    customer.lastName,
                    customer.birthDate,
                    customer.accounts.map { account -> AccountResponse(account.id, account.iban, account.currency) },
                )
            }
        )
    }
}
