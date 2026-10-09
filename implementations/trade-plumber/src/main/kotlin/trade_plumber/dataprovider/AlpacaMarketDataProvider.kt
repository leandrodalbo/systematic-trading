package trade_plumber.dataprovider

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.body
import org.springframework.web.util.UriBuilder
import trade_plumber.error.AlpacaApiFailedException
import trade_plumber.error.ErrorMessage
import trade_plumber.model.AssetClass
import trade_plumber.model.Bar
import trade_plumber.model.BarsPage
import trade_plumber.model.Timeframe
import java.math.BigDecimal
import java.time.Instant

class AlpacaMarketDataProvider(private val restClient: RestClient) : MarketDataProvider {

    override fun barsPage(
        symbol: String,
        assetClass: AssetClass,
        timeframe: Timeframe,
        from: Instant,
        to: Instant,
        pageToken: String?,
    ): BarsPage {
        val response = runCatching { fetch(symbol, assetClass, timeframe, from, to, pageToken) }
            .getOrElse { throw apiFailureException(it, symbol) }
            ?: throw AlpacaApiFailedException(symbol, ErrorMessage.ALPACA_EMPTY_RESPONSE)

        return BarsPage(
            bars = response.bars[symbol].orEmpty().map { it.toModelBar() },
            nextPageToken = response.nextPageToken,
        )
    }

    private fun fetch(
        symbol: String,
        assetClass: AssetClass,
        timeframe: Timeframe,
        from: Instant,
        to: Instant,
        pageToken: String?,
    ): BarsResponse? =
        restClient.get()
            .uri { uri ->
                uri.path(pathForAsset(assetClass))
                    .queryParam(SYMBOLS_PARAM, symbol)
                    .queryParam(TIMEFRAME_PARAM, toAlpacaTimeframe(timeframe))
                    .queryParam(START_PARAM, from.toString())
                    .queryParam(END_PARAM, to.toString())
                    .queryParam(LIMIT_PARAM, PAGE_LIMIT)
                    .addStockOnlyParams(assetClass)
                    .addPageToken(pageToken)
                    .build()
            }
            .retrieve()
            .body<BarsResponse>()

    private fun apiFailureException(ex: Throwable, symbol: String): Throwable = when (ex) {
        is RestClientResponseException ->
            AlpacaApiFailedException(symbol, ErrorMessage.ALPACA_HTTP_ERROR, ex.statusCode.value(), ex)

        is ResourceAccessException ->
            AlpacaApiFailedException(symbol, ErrorMessage.ALPACA_UNREACHABLE, cause = ex)

        else -> ex
    }

    private fun UriBuilder.addStockOnlyParams(assetClass: AssetClass): UriBuilder =
        if (assetClass == AssetClass.STOCK)
            queryParam(FEED_PARAM, FEED_PARAM_VALUE)
                .queryParam(ADJUSTMENT_PARAM, ADJUSTMENT_PARAM_VALUE)
        else
            this

    private fun UriBuilder.addPageToken(pageToken: String?): UriBuilder =
        if (pageToken != null) queryParam(PAGE_TOKEN_PARAM, pageToken) else this

    private fun pathForAsset(assetClass: AssetClass) = when (assetClass) {
        AssetClass.STOCK -> STOCKS_PATH
        AssetClass.CRYPTO -> CRYPTO_PATH
    }

    private fun toAlpacaTimeframe(timeframe: Timeframe) = when (timeframe) {
        Timeframe.HOURS1 -> TIMEFRAME_1_HOUR
        Timeframe.HOURS4 -> TIMEFRAME_4_HOURS
        Timeframe.DAY -> TIMEFRAME_1_DAY
        Timeframe.WEEK -> TIMEFRAME_1_WEEK
        Timeframe.MONTH -> TIMEFRAME_1_MONTH
    }

    private data class BarsResponse(
        val bars: Map<String, List<AlpacaBar>> = emptyMap(),
        @JsonProperty(NEXT_PAGE_TOKEN)
        val nextPageToken: String? = null,
    )

    private data class AlpacaBar(
        val t: Instant,
        val o: BigDecimal,
        val h: BigDecimal,
        val l: BigDecimal,
        val c: BigDecimal,
        val v: BigDecimal,
    ) {
        fun toModelBar() = Bar(t, o, h, l, c, v)
    }

    private companion object {
        const val PAGE_LIMIT = 10_000

        const val STOCKS_PATH = "/v2/stocks/bars"
        const val CRYPTO_PATH = "/v1beta3/crypto/us/bars"

        const val SYMBOLS_PARAM = "symbols"
        const val TIMEFRAME_PARAM = "timeframe"
        const val START_PARAM = "start"
        const val END_PARAM = "end"
        const val LIMIT_PARAM = "limit"
        const val PAGE_TOKEN_PARAM = "page_token"
        const val FEED_PARAM = "feed"
        const val FEED_PARAM_VALUE = "sip"
        const val ADJUSTMENT_PARAM = "adjustment"
        const val ADJUSTMENT_PARAM_VALUE = "all"

        const val TIMEFRAME_1_HOUR = "1Hour"
        const val TIMEFRAME_4_HOURS = "4Hour"
        const val TIMEFRAME_1_DAY = "1Day"
        const val TIMEFRAME_1_WEEK = "1Week"
        const val TIMEFRAME_1_MONTH = "1Month"

        const val NEXT_PAGE_TOKEN = "next_page_token"
    }
}
