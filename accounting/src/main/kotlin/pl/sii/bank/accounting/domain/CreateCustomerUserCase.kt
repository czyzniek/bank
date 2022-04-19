package pl.sii.bank.accounting.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.util.UUID

class CreateCustomerUserCase(
    private val customerStore: CustomerStore,
    private val externalCustomerProvider: ExternalCustomerProvider,
    private val log: Logger = LoggerFactory.getLogger(CreateCustomerUserCase::class.java)
) {
    fun execute(input: Input): Output {
        log.info("Creating customer")
        val externalCustomer = externalCustomerProvider.create(input.toExternalCustomerParams())
        val newCustomer = Customer.initialize(
            Customer.InitializeCustomer(
                externalCustomer.id,
                input.firstName,
                input.lastName,
                input.birthDate
            )
        )
        log.info("Saving newly created customer {}", newCustomer.id)
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
