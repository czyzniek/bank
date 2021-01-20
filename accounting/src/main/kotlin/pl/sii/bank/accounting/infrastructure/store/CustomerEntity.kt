package pl.sii.bank.accounting.infrastructure.store

import java.time.LocalDate
import java.util.*

data class CustomerEntity(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate
)
