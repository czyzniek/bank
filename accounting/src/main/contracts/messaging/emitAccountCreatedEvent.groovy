import org.springframework.cloud.contract.spec.Contract

Contract.make {
    label 'emit_created_account_event'
    input {
        triggeredBy 'accountCreatedTriggered()'
    }
    outputMessage {
        sentTo 'queue.created-account.messages'
        body(
            id: anyUuid(),
            iban: anyNonBlankString(),
            currency: anyOf("PLN", "EUR", "USD"),
            customerId: anyUuid()
        )
    }
}
