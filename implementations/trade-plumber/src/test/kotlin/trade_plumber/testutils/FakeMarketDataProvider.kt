package trade_plumber.testutils

import trade_plumber.dataprovider.MarketDataProvider
import trade_plumber.error.AlpacaApiFailedException
import trade_plumber.error.ErrorMessage
import trade_plumber.model.AssetClass
import trade_plumber.model.BarsPage
import trade_plumber.model.Timeframe
import java.time.Instant

/** Returns the scripted pages in order and records every request. */
class FakeMarketDataProvider(vararg pages: BarsPage, private val failOnCall: Int? = null) : MarketDataProvider {
    private val pages = pages.toList()
    val calls = mutableListOf<Call>()

    override fun barsPage(
        symbol: String,
        assetClass: AssetClass,
        timeframe: Timeframe,
        from: Instant,
        to: Instant,
        pageToken: String?,
    ): BarsPage {
        calls += Call(from, to, pageToken)
        if (calls.size == failOnCall) throw AlpacaApiFailedException(symbol, ErrorMessage.ALPACA_UNREACHABLE)
        return pages[calls.size - 1]
    }

    data class Call(val from: Instant, val to: Instant, val pageToken: String?)
}
