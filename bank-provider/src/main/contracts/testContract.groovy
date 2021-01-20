import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        url "/test"
    }
    response {
        status OK()
    }
}