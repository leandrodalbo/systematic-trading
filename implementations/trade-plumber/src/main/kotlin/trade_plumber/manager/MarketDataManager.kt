package trade_plumber.manager

import org.springframework.core.retry.RetryOperations
import org.springframework.stereotype.Service
import trade_plumber.dataprovider.MarketDataProvider
import trade_plumber.model.AssetClass
import trade_plumber.model.Bar
import trade_plumber.model.Timeframe
import trade_plumber.props.MarketDataProperties
import trade_plumber.repository.BarRepository
import java.time.Clock
import java.time.Instant
import java.util.function.Supplier

@Service
class MarketDataManager(
    private val provider: MarketDataProvider,
    private val repository: BarRepository,
    private val properties: MarketDataProperties,
    private val clock: Clock,
    private val retry: RetryOperations,
) {

    fun update(symbol: String, assetClass: AssetClass, timeframe: Timeframe) {
        when (val lastStored = repository.lastBar(symbol, assetClass, timeframe)) {
            null -> writeFullHistory(symbol, assetClass, timeframe)
            else -> resumeFrom(lastStored, symbol, assetClass, timeframe)
        }
    }

    private fun writeFullHistory(symbol: String, assetClass: AssetClass, timeframe: Timeframe) {
        val bars = barsFrom(symbol, assetClass, timeframe, historyStartFor(assetClass))
        repository.write(symbol, assetClass, timeframe, bars)
    }

    private fun resumeFrom(lastStored: Bar, symbol: String, assetClass: AssetClass, timeframe: Timeframe) {
        val fetched = barsFrom(symbol, assetClass, timeframe, lastStored.time).iterator()
        if (!fetched.hasNext()) return

        val first = fetched.next()
        if (isSameBarStart(first, lastStored)) {
            repository.replaceLastAndAppend(symbol, assetClass, timeframe, sequenceOf(first) + fetched.asSequence())
        } else {
            writeFullHistory(symbol, assetClass, timeframe)
        }
    }

    private fun isSameBarStart(fetched: Bar, stored: Bar) =
        fetched.time == stored.time && fetched.open.compareTo(stored.open) == 0

    private fun barsFrom(symbol: String, assetClass: AssetClass, timeframe: Timeframe, from: Instant): Sequence<Bar> {
        val to = clock.instant()
        return sequence {
            var pageToken: String? = null
            do {
                val page = retry.invoke(Supplier { provider.barsPage(symbol, assetClass, timeframe, from, to, pageToken) })
                yieldAll(page.bars)
                pageToken = page.nextPageToken
            } while (pageToken != null)
        }
    }

    private fun historyStartFor(assetClass: AssetClass) = when (assetClass) {
        AssetClass.STOCK -> properties.stockHistoryStart
        AssetClass.CRYPTO -> properties.cryptoHistoryStart
    }
}
