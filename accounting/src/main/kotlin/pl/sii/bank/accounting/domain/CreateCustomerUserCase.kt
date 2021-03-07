package pl.sii.bank.accounting.domain

import java.time.LocalDate
import java.util.*

class CreateCustomerUserCase(
    private val customerStore: CustomerStore,
    private val externalCustomerProvider: ExternalCustomerProvider
) {

    fun execute(input: Input): Output {
        val externalCustomer = externalCustomerProvider.create(input.toExternalCustomerParams())
        val newCustomer = Customer.initialize(
            Customer.InitializeCustomer(
                externalCustomer.id,
                input.firstName,
                input.lastName,
                input.birthDate
            )
        )
        val savedCustomer = customerStore.save(newCustomer)
        return Output(savedCustomer.id)
    }

    data class Input(
        val firstName: String,
        val lastName: String,
        val birthDate: LocalDate
    ) {
        fun toExternalCustomerParams(): ExternalCustomerProvider.CreateCustomerParams {
            return ExternalCustomerProvider.CreateCustomerParams(
                firstName, lastName, birthDate
            )
        }
    }

    data class Output(
        val id: UUID
    )
}
