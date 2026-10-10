package trade_plumber.manager

import org.springframework.stereotype.Service
import trade_plumber.dataprovider.MarketDataProvider
import trade_plumber.model.AssetClass
import trade_plumber.model.Bar
import trade_plumber.model.Timeframe
import trade_plumber.props.MarketDataProperties
import trade_plumber.repository.BarRepository
import java.time.Clock
import java.time.Instant

@Service
class MarketDataManager(
    private val provider: MarketDataProvider,
    private val repository: BarRepository,
    private val properties: MarketDataProperties,
    private val clock: Clock,
) {

    fun update(symbol: String, assetClass: AssetClass, timeframe: Timeframe) {
        when (repository.lastBar(symbol, assetClass, timeframe)) {
            null -> {
                val bars = barsFrom(symbol, assetClass, timeframe, historyStartFor(assetClass))
                repository.write(symbol, assetClass, timeframe, bars)
            }
            else -> TODO(RESUME_NOT_IMPLEMENTED)
        }
    }

    private fun barsFrom(symbol: String, assetClass: AssetClass, timeframe: Timeframe, from: Instant): Sequence<Bar> {
        val to = clock.instant()
        return sequence {
            var pageToken: String? = null
            do {
                val page = provider.barsPage(symbol, assetClass, timeframe, from, to, pageToken)
                yieldAll(page.bars)
                pageToken = page.nextPageToken
            } while (pageToken != null)
        }
    }

    private fun historyStartFor(assetClass: AssetClass) = when (assetClass) {
        AssetClass.STOCK -> properties.stockHistoryStart
        AssetClass.CRYPTO -> properties.cryptoHistoryStart
    }

    private companion object {
        const val RESUME_NOT_IMPLEMENTED = "cycle 4b: resume from the last stored bar"
    }
}
