package pl.sii.bank.transaction.domain

import java.util.*

class SaveCustomerAccountUseCase(private val customerAccountStore: CustomerAccountStore) {

    fun execute(input: Input): Output {
        val customerAccount = input.toDomain()
        customerAccountStore.save(customerAccount)
        return Output(true)
    }

    data class Input(
        val accountId: UUID,
        val iban: String,
        val currency: Currency,
        val customerId: UUID
    ) {
        fun toDomain(): CustomerAccount =
            CustomerAccount(
                accountId,
                iban,
                currency,
                customerId
            )
    }

    data class Output(
        val success: Boolean
    )
}
