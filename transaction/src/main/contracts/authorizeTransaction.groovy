import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should authorize transaction"

    request {
        method POST()
        url $(
            stub("/transactions/${regex(uuid())}/authorize"),
            test("/transactions/2b0042da-7d0e-41d8-8cc9-1958756ba01d/authorize")
        )
    }

    response {
        status NO_CONTENT()
    }
}
