package pl.sii.bank.accounting.infrastructure.web

import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.sii.bank.accounting.WebTestBase
import java.util.UUID

internal class AccountControllerTest : WebTestBase() {

    @Test
    fun `should fetch balance of given account`() {
        // given
        val accountId = UUID.randomUUID()

        // when
        val result = mockMvc.perform(get("/accounts/{accountId}/balance", accountId))

        // then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("$.accountId", equalTo(accountId.toString())))
            .andExpect(jsonPath("$.balance.amount", equalTo(1000)))
            .andExpect(jsonPath("$.balance.currency", equalTo("PLN")))
    }
}
