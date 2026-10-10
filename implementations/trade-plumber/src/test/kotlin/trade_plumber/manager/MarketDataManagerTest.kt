package trade_plumber.manager

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import trade_plumber.dataprovider.MarketDataProvider
import trade_plumber.error.AlpacaApiFailedException
import trade_plumber.error.ErrorMessage
import trade_plumber.model.AssetClass
import trade_plumber.model.BarsPage
import trade_plumber.model.Timeframe
import trade_plumber.props.MarketDataProperties
import trade_plumber.props.StorageProperties
import trade_plumber.repository.BarRepository
import trade_plumber.repository.TestBars.BAR_1
import trade_plumber.repository.TestBars.BAR_2
import trade_plumber.repository.TestBars.BAR_3
import java.nio.file.Path
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class MarketDataManagerTest {

    @TempDir
    lateinit var tempDir: Path

    private lateinit var baseDir: Path
    private lateinit var repository: BarRepository

    @BeforeEach
    fun setUp() {
        baseDir = tempDir.resolve(DATA_DIR)
        repository = BarRepository(StorageProperties(baseDir))
    }

    @Test
    fun `update with no file writes the bars from every page in order`() {
        managerWith(FakeProvider(PAGE_1, PAGE_2)).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        val expected = """
            time,open,high,low,close,volume
            2024-01-02T05:00:00Z,187.15,188.44,183.885,185.64,82488700
            2024-01-03T05:00:00Z,184.22,185.88,183.43,184.25,58414500.5
            2024-01-04T05:00:00Z,182.15,183.09,180.88,181.91,71983600
        """.trimIndent() + "\n"

        assertEquals(expected, stockFile().readText())
    }

    @Test
    fun `update passes each page token to the next request and stops after the last page`() {
        val provider = FakeProvider(PAGE_1, PAGE_2)

        managerWith(provider).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        assertEquals(listOf(null, PAGE_1_TOKEN), provider.calls.map { it.pageToken })
    }

    @Test
    fun `update requests stocks from the stock history start up to now`() {
        val provider = FakeProvider(PAGE_1, PAGE_2)

        managerWith(provider).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        assertEquals(listOf(STOCK_HISTORY_START, STOCK_HISTORY_START), provider.calls.map { it.from })
        assertEquals(listOf(NOW, NOW), provider.calls.map { it.to })
    }

    @Test
    fun `update requests crypto from the crypto history start`() {
        val provider = FakeProvider(PAGE_1, PAGE_2)

        managerWith(provider).update(CRYPTO_SYMBOL, AssetClass.CRYPTO, Timeframe.DAY)

        assertEquals(listOf(CRYPTO_HISTORY_START, CRYPTO_HISTORY_START), provider.calls.map { it.from })
    }

    @Test
    fun `update treats a header only file as no data and writes the full history`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, emptySequence())

        managerWith(FakeProvider(PAGE_1, PAGE_2)).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        assertEquals(BAR_3, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    @Test
    fun `update with a provider failure on page 2 throws and creates no file`() {
        val provider = FakeProvider(PAGE_1, PAGE_2, failOnCall = 2)

        assertFailsWith<AlpacaApiFailedException> {
            managerWith(provider).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)
        }

        assertFalse(stockFile().exists())
    }

    @Test
    fun `update with no bars from the provider writes a header only file`() {
        managerWith(FakeProvider(BarsPage(emptyList(), null))).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        assertEquals("time,open,high,low,close,volume\n", stockFile().readText())
    }

    private fun managerWith(provider: MarketDataProvider) = MarketDataManager(
        provider = provider,
        repository = repository,
        properties = MarketDataProperties(STOCK_HISTORY_START, CRYPTO_HISTORY_START),
        clock = Clock.fixed(NOW, ZoneOffset.UTC),
    )

    private fun stockFile() = baseDir.resolve("stocks/AAPL_DAY.csv")

    private data class Call(val from: Instant, val to: Instant, val pageToken: String?)

    /** Returns the scripted pages in order and records every request. */
    private class FakeProvider(vararg pages: BarsPage, private val failOnCall: Int? = null) : MarketDataProvider {
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
    }

    private companion object {
        const val DATA_DIR = "data"
        const val STOCK_SYMBOL = "AAPL"
        const val CRYPTO_SYMBOL = "BTC/USD"
        const val PAGE_1_TOKEN = "page-2-please"

        val NOW: Instant = Instant.parse("2024-01-05T00:00:00Z")
        val STOCK_HISTORY_START: Instant = Instant.parse("2016-01-01T00:00:00Z")
        val CRYPTO_HISTORY_START: Instant = Instant.parse("2021-01-01T00:00:00Z")

        val PAGE_1 = BarsPage(listOf(BAR_1, BAR_2), PAGE_1_TOKEN)
        val PAGE_2 = BarsPage(listOf(BAR_3), null)
    }
}
