import org.springframework.cloud.contract.spec.Contract

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

Contract.make {
    description "Should initialize transaction"

    request {
        method GET()
        url $(
            stub("/transactions/${regex(uuid())}/status"),
            test("/transactions/c0dd4aa6-504f-411a-8010-584cbd8ff210/status")
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
            status: anyOf("INITIALIZED", "CONFIRMED", "AUTHORIZED")
        )
    }
}
