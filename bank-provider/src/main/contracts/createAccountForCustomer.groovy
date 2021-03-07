import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url regex("/api/customer/${uuid()}/account")
        headers {
            contentType applicationXml()
        }
        body """
            <account>
                <currency>PLN</currency>
            </account>
        """
        bodyMatchers {
            xPath('/account/currency/text()', byRegex("PLN|USD|EUR"))
        }
    }
    response {
        status CREATED()
        headers {
            contentType applicationXml()
        }
        body """
            <account>
                <id>${UUID.randomUUID()}</id>
                <iban>PL71147010414407315781808829</iban>
            </account>
        """
    }
}
