import org.springframework.cloud.contract.spec.Contract

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

Contract.make {
    description "Should confirm transaction"

    request {
        method POST()
        url $(
            stub("/transactions/${regex(uuid())}/confirm"),
            test("/transactions/c0dd4aa6-504f-411a-8010-584cbd8ff210/confirm")
        )
        headers {
            contentType applicationJson()
        }
        body(
            sourceAccount: $(
                stub(anyUuid()),
                test("472d0334-95ce-4767-8a5a-13eaa2d38859")
            )
        )
    }

    response {
        status NO_CONTENT()
    }
}
