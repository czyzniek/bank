package pl.sii.bank.accounting.infrastructure.web

import java.time.LocalDate

data class CreateCustomerRequest(
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate
)
