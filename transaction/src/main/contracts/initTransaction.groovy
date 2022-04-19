import org.springframework.cloud.contract.spec.Contract

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

Contract.make {
    description "Should initialize transaction"

    request {
        method POST()
        url "/transactions"
        headers {
            contentType(APPLICATION_JSON_VALUE)
        }
        body(
            targetAccount: $(
                stub(anyUuid()),
                test("c992a432-3082-4d08-9f0a-8e2e11e29718")
            ),
            money: [
                amount  : $(
                    stub(anyNumber()),
                    test("1000.00")
                ),
                currency: $(
                    stub(anyOf("PLN", "USD", "EUR")),
                    test("PLN")
                )
            ],
            note: $(
                stub(optional(anyNonBlankString())),
                test("Transaction note"))
        )
    }

    response {
        status CREATED()
        headers {
            contentType applicationJson()
        }
        body([
            transactionId: anyUuid()
        ])
    }
}
