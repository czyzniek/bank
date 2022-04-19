package pl.sii.bank.accounting.domain

import pl.sii.bank.accounting.infrastructure.store.CustomerEntity
import java.time.LocalDate
import java.util.UUID

class Customer(
    val id: UUID,
    val externalId: UUID,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val accounts: MutableList<Account>
) {

    companion object Factory {
        fun initialize(command: InitializeCustomer): Customer =
            Customer(
                UUID.randomUUID(),
                command.externalId,
                command.firstName,
                command.lastName,
                command.birthDate,
                mutableListOf()
            )

        fun create(customerEntity: CustomerEntity, accounts: MutableList<Account>): Customer =
            Customer(
                customerEntity.id,
                customerEntity.externalId,
                customerEntity.firstName,
                customerEntity.lastName,
                customerEntity.birthDate,
                accounts
            )
    }

    data class InitializeCustomer(
        val externalId: UUID,
        val firstName: String,
        val lastName: String,
        val birthDate: LocalDate
    )
}
