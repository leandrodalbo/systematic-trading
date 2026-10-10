package trade_plumber.manager

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import trade_plumber.dataprovider.MarketDataProvider
import trade_plumber.error.AlpacaApiFailedException
import trade_plumber.testutils.FakeMarketDataProvider.Call
import trade_plumber.model.AssetClass
import trade_plumber.model.BarsPage
import trade_plumber.model.Timeframe
import trade_plumber.props.MarketDataProperties
import trade_plumber.props.StorageProperties
import trade_plumber.repository.BarRepository
import trade_plumber.testutils.FakeMarketDataProvider
import trade_plumber.testutils.TestBars.BAR_1
import trade_plumber.testutils.TestBars.BAR_2
import trade_plumber.testutils.TestBars.BAR_2_REVISED
import trade_plumber.testutils.TestBars.BAR_3
import java.math.BigDecimal
import java.nio.file.Path
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlin.io.path.readText
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MarketDataManagerResumeTest {

    @TempDir
    lateinit var tempDir: Path

    private lateinit var baseDir: Path
    private lateinit var repository: BarRepository

    @BeforeEach
    fun setUp() {
        baseDir = tempDir.resolve(DATA_DIR)
        repository = BarRepository(StorageProperties(baseDir))
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_2))
    }

    @Test
    fun `matching first bar replaces the last row and appends the newer bars`() {
        val provider = FakeMarketDataProvider(BarsPage(listOf(BAR_2_REVISED, BAR_3), null))

        managerWith(provider).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        val expected = """
            time,open,high,low,close,volume
            2024-01-02T05:00:00Z,187.15,188.44,183.885,185.64,82488700
            2024-01-03T05:00:00Z,184.22,185.88,183.43,184.30,58500000
            2024-01-04T05:00:00Z,182.15,183.09,180.88,181.91,71983600
        """.trimIndent() + "\n"

        assertEquals(expected, stockFile().readText())
    }

    @Test
    fun `resume requests from the last stored bar time up to now`() {
        val provider = FakeMarketDataProvider(BarsPage(listOf(BAR_2_REVISED, BAR_3), null))

        managerWith(provider).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        assertEquals(listOf(Call(BAR_2.time, NOW, null)), provider.calls)
    }

    @Test
    fun `open with a different scale still counts as a match`() {
        val sameOpenMoreDigits = BAR_2_REVISED.copy(open = BigDecimal("184.2200"))
        val provider = FakeMarketDataProvider(BarsPage(listOf(sameOpenMoreDigits, BAR_3), null))

        managerWith(provider).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        assertEquals(listOf(BAR_2.time), provider.calls.map { it.from })
    }

    @Test
    fun `matching resume over two pages follows the token and appends every bar`() {
        val provider = FakeMarketDataProvider(
            BarsPage(listOf(BAR_2_REVISED), PAGE_TOKEN),
            BarsPage(listOf(BAR_3), null),
        )

        managerWith(provider).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        assertEquals(listOf(null, PAGE_TOKEN), provider.calls.map { it.pageToken })
        assertEquals(BAR_3, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    @Test
    fun `different open means adjusted prices so the full history is rewritten`() {
        val adjustedBar2 = BAR_2.copy(open = BigDecimal("180.00"))
        val provider = FakeMarketDataProvider(
            BarsPage(listOf(adjustedBar2, BAR_3), null),
            BarsPage(listOf(BAR_1, adjustedBar2, BAR_3), null),
        )

        managerWith(provider).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        val expected = """
            time,open,high,low,close,volume
            2024-01-02T05:00:00Z,187.15,188.44,183.885,185.64,82488700
            2024-01-03T05:00:00Z,180.00,185.88,183.43,184.25,58414500.5
            2024-01-04T05:00:00Z,182.15,183.09,180.88,181.91,71983600
        """.trimIndent() + "\n"

        assertEquals(expected, stockFile().readText())
        assertEquals(listOf(BAR_2.time, STOCK_HISTORY_START), provider.calls.map { it.from })
    }

    @Test
    fun `first bar at a different time than the last stored bar rewrites the full history`() {
        val provider = FakeMarketDataProvider(
            BarsPage(listOf(BAR_3), null),
            BarsPage(listOf(BAR_1, BAR_2, BAR_3), null),
        )

        managerWith(provider).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        assertEquals(listOf(BAR_2.time, STOCK_HISTORY_START), provider.calls.map { it.from })
        assertEquals(BAR_3, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    @Test
    fun `resume with no bars leaves the file unchanged and makes no other request`() {
        val original = stockFile().readText()
        val provider = FakeMarketDataProvider(BarsPage(emptyList(), null))

        managerWith(provider).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        assertEquals(original, stockFile().readText())
        assertEquals(1, provider.calls.size)
    }

    @Test
    fun `provider failure on page 2 of the resume throws and keeps the original file`() {
        val original = stockFile().readText()
        val provider = FakeMarketDataProvider(
            BarsPage(listOf(BAR_2_REVISED), PAGE_TOKEN),
            BarsPage(listOf(BAR_3), null),
            failOnCall = 2,
        )

        assertFailsWith<AlpacaApiFailedException> {
            managerWith(provider).update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)
        }

        assertEquals(original, stockFile().readText())
    }

    private fun managerWith(provider: MarketDataProvider) = MarketDataManager(
        provider = provider,
        repository = repository,
        properties = MarketDataProperties(STOCK_HISTORY_START, CRYPTO_HISTORY_START),
        clock = Clock.fixed(NOW, ZoneOffset.UTC),
    )

    private fun stockFile() = baseDir.resolve("stocks/AAPL_DAY.csv")

    private companion object {
        const val DATA_DIR = "data"
        const val STOCK_SYMBOL = "AAPL"
        const val PAGE_TOKEN = "page-2-please"

        val NOW: Instant = Instant.parse("2024-01-05T00:00:00Z")
        val STOCK_HISTORY_START: Instant = Instant.parse("2016-01-01T00:00:00Z")
        val CRYPTO_HISTORY_START: Instant = Instant.parse("2021-01-01T00:00:00Z")
    }
}
