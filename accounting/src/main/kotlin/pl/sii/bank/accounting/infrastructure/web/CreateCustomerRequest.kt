package pl.sii.bank.accounting.infrastructure.web

import java.time.LocalDate
import javax.validation.constraints.Size

data class CreateCustomerRequest(
    @field:Size(max = 25)
    val firstName: String,
    @field:Size(max = 25)
    val lastName: String,
    val birthDate: LocalDate
)
