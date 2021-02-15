package pl.sii.bank.transaction.domain

import java.util.*

class SaveCustomerAccountUseCase {

    fun execute(input: Input): Output {
        return Output(true)
    }

    data class Input(
        val accountId: UUID,
        val iban: String,
        val currency: Currency,
        val customerId: UUID
    )

    data class Output(
        val success: Boolean
    )
}
