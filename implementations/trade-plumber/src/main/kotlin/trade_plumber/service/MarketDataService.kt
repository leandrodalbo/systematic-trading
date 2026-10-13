package trade_plumber.service

import org.springframework.stereotype.Service
import trade_plumber.manager.MarketDataManager
import trade_plumber.model.AssetClass
import trade_plumber.model.Timeframe

@Service
class MarketDataService(
    private val manager: MarketDataManager,
) {

    fun update(symbol: String, assetClass: AssetClass, timeframe: Timeframe) =
        manager.update(symbol, assetClass, timeframe)
}
