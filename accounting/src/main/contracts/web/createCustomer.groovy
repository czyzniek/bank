import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should create customer'

    request {
        method POST()
        url '/customers'
        headers {
            contentType applicationJson()
        }
        body(
            firstName: anyNonBlankString(),
            lastName: anyNonBlankString(),
            birthDate: anyDate()
        )
    }

    response {
        status CREATED()
        headers {
            contentType applicationJson()
        }
        body(
            customerId: anyUuid()
        )
    }
}
