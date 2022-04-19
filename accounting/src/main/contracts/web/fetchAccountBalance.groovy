import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should returns balance for given account'

    request {
        method GET()
        url $(
            stub(regex("/accounts/${uuid()}/balance")),
            test("/accounts/4b5dbd29-865e-4781-be21-a031a6a7cdb0/balance")
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
            accountId: fromRequest().path(1),
            balance: [
                amount  : $(
                    stub("1000.00"),
                    test(regex("[1-9][0-9]{0,10}|0\\.0[1-9]|0\\.[1-9][0-9]?|[1-9][0-9]{0,10}\\.[0-9]{1,2}"))
                ),
                currency: $(
                    stub(anyOf("PLN", "USD", "EUR")),
                    test("PLN")
                )
            ]
        )
    }
}
