package trade_plumber.service

import org.springframework.stereotype.Service
import trade_plumber.error.BatchUpdateFailedException
import trade_plumber.manager.MarketDataManager
import trade_plumber.model.AssetClass
import trade_plumber.model.Timeframe
import trade_plumber.requests.UpdateListRequest

@Service
class MarketDataService(
    private val manager: MarketDataManager,
) {

    fun update(symbol: String, assetClass: AssetClass, timeframe: Timeframe) =
        manager.update(symbol, assetClass, timeframe)

    fun updateAll(batch: UpdateListRequest) =
        batch.updates.forEach { request ->
            runCatching { update(request.symbol, request.assetClass, request.timeframe) }
                .onFailure { throw BatchUpdateFailedException(request.symbol, it) }
        }
}
