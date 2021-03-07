import org.springframework.cloud.contract.spec.Contract

def ibanRegex = "[A-Z]{2}[A-Z0-9]{11,32}"

Contract.make {
    request {
        method GET()
        url regex("/api/accounts/$ibanRegex/balance")
    }

    response {
        status OK()
        headers {
            contentType applicationXml()
        }
        body """
            <balance>
                <amount>12345.67</amount>
                <currency>PLN</currency>
            </balance>
        """
    }
}
