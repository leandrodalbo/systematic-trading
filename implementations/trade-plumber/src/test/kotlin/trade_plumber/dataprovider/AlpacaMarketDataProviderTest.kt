package trade_plumber.dataprovider

import org.hamcrest.Matchers.startsWith
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.RequestMatcher
import org.springframework.test.web.client.match.MockRestRequestMatchers.header
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder
import trade_plumber.conf.AlpacaConfig
import trade_plumber.model.AssetClass
import trade_plumber.model.Bar
import trade_plumber.model.BarsPage
import trade_plumber.model.Timeframe
import trade_plumber.props.AlpacaProperties
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AlpacaMarketDataProviderTest {

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

    @Test
    fun `uses the data url and sends the auth headers`() {
        server.expect(requestTo(startsWith("$DATA_URL$STOCKS_PATH")))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header("APCA-API-KEY-ID", TEST_KEY))
            .andExpect(header("APCA-API-SECRET-KEY", TEST_SECRET))
            .andRespond(json(EMPTY_RESPONSE))

        stockPage()

        server.verify()
    }

    @Test
    fun `stock request uses the stock endpoint with sip feed and all adjustments`() {
        server.expect(requestTo(startsWith("$DATA_URL$STOCKS_PATH")))
            .andExpect(queryParam("feed", "sip"))
            .andExpect(queryParam("adjustment", "all"))
            .andRespond(json(EMPTY_RESPONSE))

        stockPage()

        server.verify()
    }

    @Test
    fun `crypto request uses the crypto endpoint without stock-only params`() {
        server.expect(requestTo(startsWith("$DATA_URL$CRYPTO_PATH")))
            .andExpect(queryParam("symbols", "BTC/USD"))
            .andExpect(noQueryParam("feed"))
            .andExpect(noQueryParam("adjustment"))
            .andRespond(json(EMPTY_RESPONSE))

        cryptoPage()

        server.verify()
    }

    @Test
    fun `sends symbol, timeframe, start, end and limit`() {
        server.expect(requestTo(startsWith("$DATA_URL$STOCKS_PATH")))
            .andExpect(queryParam("symbols", "AAPL"))
            .andExpect(queryParam("timeframe", "1Day"))
            .andExpect(queryParam("start", "2024-01-01T00:00:00Z"))
            .andExpect(queryParam("end", "2024-01-31T00:00:00Z"))
            .andExpect(queryParam("limit", "10000"))
            .andRespond(json(EMPTY_RESPONSE))

        stockPage()

        server.verify()
    }

    @ParameterizedTest
    @CsvSource(
        "HOURS1, 1Hour",
        "HOURS4, 4Hour",
        "DAY, 1Day",
        "WEEK, 1Week",
        "MONTH, 1Month",
    )
    fun `maps our timeframe to the alpaca timeframe`(timeframe: Timeframe, alpacaTimeframe: String) {
        server.expect(requestTo(startsWith("$DATA_URL$STOCKS_PATH")))
            .andExpect(queryParam("timeframe", alpacaTimeframe))
            .andRespond(json(EMPTY_RESPONSE))

        stockPage(timeframe = timeframe)

        server.verify()
    }

    @Test
    fun `does not send page_token on the first page`() {
        server.expect(requestTo(startsWith("$DATA_URL$STOCKS_PATH")))
            .andExpect(noQueryParam("page_token"))
            .andRespond(json(EMPTY_RESPONSE))

        stockPage(pageToken = null)

        server.verify()
    }

    @Test
    fun `sends page_token when given`() {
        server.expect(requestTo(startsWith("$DATA_URL$STOCKS_PATH")))
            .andExpect(queryParam("page_token", "page-2"))
            .andRespond(json(EMPTY_RESPONSE))

        stockPage(pageToken = "page-2")

        server.verify()
    }

    @Test
    fun `maps alpaca bars to our Bar model`() {
        server.expect(requestTo(startsWith("$DATA_URL$STOCKS_PATH")))
            .andRespond(
                json(
                    barsResponse(
                        "AAPL",
                        nextPageToken = null,
                        barJson("2024-01-02T05:00:00Z", "187.15", "188.44", "183.885", "185.64", "82488674"),
                    )
                )
            )

        val page = stockPage()

        assertEquals(
            listOf(
                Bar(
                    time = Instant.parse("2024-01-02T05:00:00Z"),
                    open = BigDecimal("187.15"),
                    high = BigDecimal("188.44"),
                    low = BigDecimal("183.885"),
                    close = BigDecimal("185.64"),
                    volume = BigDecimal("82488674"),
                )
            ),
            page.bars,
        )
    }

    @Test
    fun `keeps crypto volume decimals`() {
        server.expect(requestTo(startsWith("$DATA_URL$CRYPTO_PATH")))
            .andRespond(
                json(
                    barsResponse(
                        "BTC/USD",
                        nextPageToken = null,
                        barJson("2024-01-01T06:00:00Z", "42280.1", "44175.5", "42200.0", "44168.2", "0.531245"),
                    )
                )
            )

        val page = cryptoPage()

        assertEquals(BigDecimal("0.531245"), page.bars.single().volume)
        assertEquals(Instant.parse("2024-01-01T06:00:00Z"), page.bars.single().time)
    }

    @Test
    fun `returns the next page token from the response`() {
        server.expect(requestTo(startsWith("$DATA_URL$STOCKS_PATH")))
            .andRespond(
                json(
                    barsResponse(
                        "AAPL",
                        nextPageToken = "page-2",
                        barJson("2024-01-02T05:00:00Z", "1", "1", "1", "1", "100"),
                    )
                )
            )

        val page = stockPage()

        assertEquals("page-2", page.nextPageToken)
    }

    @Test
    fun `returns an empty page when alpaca has no bars for the range`() {
        server.expect(requestTo(startsWith("$DATA_URL$STOCKS_PATH")))
            .andRespond(json(EMPTY_RESPONSE))

        val page = stockPage()

        assertEquals(BarsPage(bars = emptyList(), nextPageToken = null), page)
    }

    private fun stockPage(timeframe: Timeframe = Timeframe.DAY, pageToken: String? = null) =
        provider.barsPage("AAPL", AssetClass.STOCK, timeframe, FROM, TO, pageToken)

    private fun cryptoPage() =
        provider.barsPage("BTC/USD", AssetClass.CRYPTO, Timeframe.DAY, FROM, TO, null)

    private fun json(body: String) = withSuccess(body, MediaType.APPLICATION_JSON)

    private fun noQueryParam(name: String) = RequestMatcher { request ->
        assertNull(
            UriComponentsBuilder.fromUri(request.uri).build().queryParams[name],
            "query param '$name' should not be sent",
        )
    }

    private fun barsResponse(symbol: String, nextPageToken: String?, vararg bars: String): String {
        val token = nextPageToken?.let { "\"$it\"" } ?: "null"
        return """{"bars": {"$symbol": [${bars.joinToString(",")}]}, "next_page_token": $token}"""
    }

    private fun barJson(t: String, o: String, h: String, l: String, c: String, v: String) =
        """{"t": "$t", "o": $o, "h": $h, "l": $l, "c": $c, "v": $v, "n": 1, "vw": $c}"""

    private companion object {
        const val TEST_KEY = "test-key"
        const val TEST_SECRET = "test-secret"
        const val DATA_URL = "https://data.alpaca.markets"
        const val STOCKS_PATH = "/v2/stocks/bars"
        const val CRYPTO_PATH = "/v1beta3/crypto/us/bars"
        const val EMPTY_RESPONSE = """{"bars": {}, "next_page_token": null}"""

        val FROM: Instant = Instant.parse("2024-01-01T00:00:00Z")
        val TO: Instant = Instant.parse("2024-01-31T00:00:00Z")
    }
}
