package pl.sii.bank.accounting.infrastructure.web

import java.time.LocalDate
import javax.validation.constraints.Max
import javax.validation.constraints.Pattern

data class CreateCustomerRequest(
    @Max(25)
    val firstName: String,
    @Max(25)
    val lastName: String,
    @Pattern(regexp = "yyyy-MM-dd")
    val birthDate: LocalDate
)
