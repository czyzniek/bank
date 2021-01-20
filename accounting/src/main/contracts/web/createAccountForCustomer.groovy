import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should create customer'

    request {
        method POST()
        url $(
            stub(regex("/customers/${uuid()}/accounts")),
            test("/customers/7b4e6ace-5c2c-45c7-b6d5-a59e37b3a34c/accounts")
        )
        headers {
            contentType applicationJson()
        }
        body(
            currency: anyOf("PLN", "USD", "EUR")
        )
    }

    response {
        status CREATED()
        headers {
            contentType applicationJson()
        }
        body(
            accountId: anyUuid()
        )
    }
}
