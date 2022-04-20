import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url regex("/api/transfers")
        headers {
            contentType applicationXml()
        }
        body """
            <transfer>
                <from>PLN</from>
                <to>PLN</to>
                <amount>PLN</amount>
                <currency>PLN</currency>
            </transfer>
        """
        bodyMatchers {
            xPath('/transfer/from/text()', byRegex("[A-Z]{2}[A-Z0-9]{11,32}"))
            xPath('/transfer/to/text()', byRegex("[A-Z]{2}[A-Z0-9]{11,32}"))
            xPath('/transfer/amount/text()', byRegex(number()))
            xPath('/transfer/currency/text()', byRegex("PLN|USD|EUR"))
        }
    }
    response {
        status CREATED()
        headers {
            contentType applicationXml()
        }
        body """
            <transfer>
                <id>${UUID.randomUUID()}</id>
            </transfer>
        """
    }
}
