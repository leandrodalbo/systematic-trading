package trade_plumber.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import trade_plumber.model.AssetClass
import trade_plumber.model.Timeframe
import trade_plumber.service.MarketDataService

@WebMvcTest(MarketDataController::class)
class MarketDataControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var service: MarketDataService

    @Test
    fun `valid update request returns 204 and starts the update`() {
        justRun { service.update(CRYPTO_SYMBOL, AssetClass.CRYPTO, Timeframe.DAY) }

        mockMvc.post(UPDATES_PATH) {
            contentType = MediaType.APPLICATION_JSON
            content = UPDATE_BODY
        }.andExpect {
            status { isNoContent() }
        }

        verify(exactly = 1) { service.update(CRYPTO_SYMBOL, AssetClass.CRYPTO, Timeframe.DAY) }
    }

    private companion object {
        const val UPDATES_PATH = "/market-data/updates"
        const val CRYPTO_SYMBOL = "BTC/USD"
        const val UPDATE_BODY = """{"symbol":"BTC/USD","assetClass":"CRYPTO","timeframe":"DAY"}"""
    }
}
