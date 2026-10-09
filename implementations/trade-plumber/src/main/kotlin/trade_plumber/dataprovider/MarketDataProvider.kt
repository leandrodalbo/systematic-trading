package trade_plumber.dataprovider

import trade_plumber.model.AssetClass
import trade_plumber.model.BarsPage
import trade_plumber.model.Timeframe
import java.time.Instant

interface MarketDataProvider {
    fun barsPage(
        symbol: String,
        assetClass: AssetClass,
        timeframe: Timeframe,
        from: Instant,
        to: Instant,
        pageToken: String?,
    ): BarsPage
}
