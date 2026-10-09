package trade_plumber.dataprovider

import org.hamcrest.Matchers.startsWith
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withException
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestClient
import trade_plumber.conf.AlpacaConfig
import trade_plumber.error.AlpacaApiFailedException
import trade_plumber.error.ErrorMessage
import trade_plumber.model.AssetClass
import trade_plumber.model.Timeframe
import trade_plumber.props.AlpacaProperties
import java.io.IOException
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AlpacaMarketDataProviderErrorTest {

    private val properties = AlpacaProperties(
        keyId = TEST_KEY,
        secretKey = TEST_SECRET,
        dataUrl = DATA_URL,
    )

    private lateinit var server: MockRestServiceServer
    private lateinit var provider: MarketDataProvider

    @BeforeEach
    fun setUp() {
        val builder = RestClient.builder()
        server = MockRestServiceServer.bindTo(builder).build()
        provider = AlpacaConfig(properties).alpacaMarketDataProvider(builder)
    }

    @ParameterizedTest
    @CsvSource(
        "401, false",
        "403, false",
        "422, false",
        "429, true",
        "500, true",
        "503, true",
    )
    fun `error status throws our exception with the status and retryable flag`(status: Int, retryable: Boolean) {
        server.expect(requestTo(startsWith("$DATA_URL$STOCKS_PATH")))
            .andRespond(withStatus(HttpStatus.valueOf(status)))

        val ex = assertFailsWith<AlpacaApiFailedException> { stockPage() }

        assertEquals(SYMBOL, ex.symbol)
        assertEquals(ErrorMessage.ALPACA_HTTP_ERROR, ex.error)
        assertEquals(status, ex.status)
        assertEquals(retryable, ex.isRetryable())
    }

    @Test
    fun `network failure throws unreachable, retryable, and keeps the cause`() {
        server.expect(requestTo(startsWith("$DATA_URL$STOCKS_PATH")))
            .andRespond(withException(IOException("connection reset")))

        val ex = assertFailsWith<AlpacaApiFailedException> { stockPage() }

        assertEquals(SYMBOL, ex.symbol)
        assertEquals(ErrorMessage.ALPACA_UNREACHABLE, ex.error)
        assertNull(ex.status)
        assertTrue(ex.isRetryable())
        assertIs<IOException>(ex.cause?.cause)
    }

    @Test
    fun `empty body throws empty response, not retryable`() {
        server.expect(requestTo(startsWith("$DATA_URL$STOCKS_PATH")))
            .andRespond(withSuccess("", MediaType.APPLICATION_JSON))

        val ex = assertFailsWith<AlpacaApiFailedException> { stockPage() }

        assertEquals(SYMBOL, ex.symbol)
        assertEquals(ErrorMessage.ALPACA_EMPTY_RESPONSE, ex.error)
        assertNull(ex.status)
        assertFalse(ex.isRetryable())
    }

    private fun stockPage() =
        provider.barsPage(SYMBOL, AssetClass.STOCK, Timeframe.DAY, FROM, TO, null)

    private companion object {
        const val SYMBOL = "AAPL"
        const val TEST_KEY = "test-key"
        const val TEST_SECRET = "test-secret"
        const val DATA_URL = "https://data.alpaca.markets"
        const val STOCKS_PATH = "/v2/stocks/bars"

        val FROM: Instant = Instant.parse("2024-01-01T00:00:00Z")
        val TO: Instant = Instant.parse("2024-01-31T00:00:00Z")
    }
}
