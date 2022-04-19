package pl.sii.bank.accounting.domain

import java.time.LocalDate
import java.util.UUID

interface ExternalCustomerProvider {

    fun create(params: CreateCustomerParams): ExternalCustomer

    data class CreateCustomerParams(
        val firstName: String,
        val lastName: String,
        val birthDate: LocalDate
    )

    data class ExternalCustomer(
        val id: UUID,
        val firstName: String,
        val lastName: String,
        val birthDate: LocalDate
    )
}
