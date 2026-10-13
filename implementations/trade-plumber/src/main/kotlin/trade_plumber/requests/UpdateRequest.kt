package trade_plumber.requests

import trade_plumber.model.AssetClass
import trade_plumber.model.Timeframe

data class UpdateRequest(
    val symbol: String,
    val assetClass: AssetClass,
    val timeframe: Timeframe,
)
