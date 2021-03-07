import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url "/api/customers"
        headers {
            contentType applicationXml()
        }
        body """
<customer>
    <firstName>Zenon</firstName>
    <lastName>Nowak</lastName>
    <birthDate>1990-11-23</birthDate>
</customer>
"""
        bodyMatchers {
            xPath('/customer/firstName/text()', byRegex(nonBlank()))
            xPath('/customer/lastName/text()', byRegex(nonBlank()))
            xPath('/customer/birthDate/text()', byDate())
        }
    }
    response {
        status CREATED()
        headers {
            contentType applicationXml()
        }
        body """
<customer>
    <id>${UUID.randomUUID()}</id>
</customer>
"""
    }
}
