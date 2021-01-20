package pl.sii.bank.accounting.infrastructure.web

import com.fasterxml.jackson.annotation.JsonFormat
import pl.sii.bank.accounting.domain.Currency
import java.time.LocalDate
import java.util.*

data class CustomerResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val birthDate: LocalDate,
    val accounts: List<AccountResponse>
)

data class AccountResponse(
    val id: UUID,
    val iban: String,
    val currency: Currency
)
