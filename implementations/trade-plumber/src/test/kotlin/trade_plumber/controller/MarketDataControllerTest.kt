package trade_plumber.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import trade_plumber.error.ErrorMessage
import trade_plumber.error.UpdateInProgressException
import trade_plumber.model.AssetClass
import trade_plumber.model.Timeframe
import trade_plumber.service.MarketDataService
import trade_plumber.testutils.TestErrors

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

    @Test
    fun `alpaca failure returns 502 with the error detail`() {
        val failure = TestErrors.unreachable()
        every { service.update(any(), any(), any()) } throws failure

        postUpdate(UPDATE_BODY).andExpect {
            status { isBadGateway() }
            jsonPath(DETAIL_PATH) { value(failure.message) }
        }
    }

    @Test
    fun `update already running returns 409 with the error detail`() {
        every { service.update(any(), any(), any()) } throws UpdateInProgressException()

        postUpdate(UPDATE_BODY).andExpect {
            status { isConflict() }
            jsonPath(DETAIL_PATH) { value(ErrorMessage.UPDATE_IN_PROGRESS.message) }
        }
    }

    @Test
    fun `unreadable body returns 400 and never calls the service`() {
        postUpdate(UNKNOWN_ASSET_CLASS_BODY).andExpect {
            status { isBadRequest() }
        }

        verify(exactly = 0) { service.update(any(), any(), any()) }
    }

    @Test
    fun `unexpected error returns 500 without leaking its message`() {
        every { service.update(any(), any(), any()) } throws IllegalStateException(INTERNAL_MESSAGE)

        postUpdate(UPDATE_BODY).andExpect {
            status { isInternalServerError() }
            jsonPath(DETAIL_PATH) { value(UNEXPECTED_ERROR_DETAIL) }
        }
    }

    private fun postUpdate(body: String) =
        mockMvc.post(UPDATES_PATH) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }

    private companion object {
        const val UPDATES_PATH = "/market-data/updates"
        const val CRYPTO_SYMBOL = "BTC/USD"
        const val UPDATE_BODY = """{"symbol":"BTC/USD","assetClass":"CRYPTO","timeframe":"DAY"}"""
        const val UNKNOWN_ASSET_CLASS_BODY = """{"symbol":"BTC/USD","assetClass":"FOREX","timeframe":"DAY"}"""
        const val DETAIL_PATH = "$.detail"
        const val INTERNAL_MESSAGE = "db password is hunter2"
        const val UNEXPECTED_ERROR_DETAIL = "Unexpected error"
    }
}
