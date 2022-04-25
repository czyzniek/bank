package pl.sii.bank.accounting.infrastructure.web

import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.sii.bank.accounting.WebTestBase
import java.util.UUID

internal class CustomerControllerTest : WebTestBase() {

    @Test
    fun `should validate customer input during creation`() {
        // given
        val request = """
        {
            "firstName": "aslkdfjsldjfhsildhfdilfgbdlifbglidfbgdlsfig",
            "lastName": "Nowak",
            "birthDate": "1990-11-23"
        }
        """.trimIndent()

        // when
        val result = mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
        // then
        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should create account for customer`() {
        // given
        val customerId = UUID.randomUUID()

        // when
        val result = mockMvc.perform(
            post("/customers/{customerId}/accounts", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "currency": "EUR"
                    }
                    """.trimIndent()
                )
        )

        // then
        result.andExpect(status().isCreated)
            .andExpect(jsonPath("$.accountId", notNullValue()))
    }
}
