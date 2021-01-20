package pl.sii.bank.accounting.domain

import java.time.LocalDate
import java.util.*

class CreateCustomerUserCase(private val customerStore: CustomerStore) {

    fun execute(input: Input): Output {
        val newCustomer = Customer.initialize(
            Customer.InitializeCustomer(
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
    )

    data class Output(
        val id: UUID
    )
}
