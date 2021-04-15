import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'should fetch all customers'

    request {
        method GET()
        url '/customers'
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
            customers: [
                [
                    id: "cd863f1f-a3c5-4b93-9fe7-572e00df6783",
                    firstName: "Zenon",
                    lastName: "Nowak",
                    birthDate: "1989-10-02",
                    accounts: [
                        [
                            id: "99027286-95f6-41f2-8b5c-57674e249e4e",
                            iban: "PL65124035237724260404434622",
                            currency: "PLN"
                        ],
                        [
                            id: "58fad162-efc0-4eae-8175-b2707276c3ba",
                            iban: "PL16845610198388776832799509",
                            currency: "EUR"
                        ]
                    ]
                ],
                [
                    id: "522480e9-6ec7-49d9-a400-c90fa283caf6",
                    firstName: "Jan",
                    lastName: "Pustak",
                    birthDate: "1979-07-12",
                    accounts: []
                ]
            ]
        )
        bodyMatchers {
            jsonPath('$.customers', byType { minOccurrence(0) })
            jsonPath('$.customers[*].id', byRegex(uuid()).asString())
            jsonPath('$.customers[*].firstName', byRegex(nonBlank()).asString())
            jsonPath('$.customers[*].lastName', byRegex(nonBlank()).asString())
            jsonPath('$.customers[*].birthDate', byRegex(isoDate()).asString())
            jsonPath('$.customers[*].accounts', byType { minOccurrence(0) })
            jsonPath('$.customers[*].accounts[*].id', byRegex(uuid()).asString())
            jsonPath('$.customers[*].accounts[*].iban', byRegex("[A-Z]{2}[A-Z0-9]{11,32}").asString())
            jsonPath('$.customers[*].accounts[*].currency', byRegex("PLN|EUR|USD").asString())
        }
    }
}
