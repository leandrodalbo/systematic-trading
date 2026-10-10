package trade_plumber.manager

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.http.HttpStatus
import trade_plumber.dataprovider.MarketDataProvider
import trade_plumber.conf.RetryConfig
import trade_plumber.error.AlpacaApiFailedException
import trade_plumber.model.AssetClass
import trade_plumber.model.BarsPage
import trade_plumber.model.Timeframe
import trade_plumber.props.MarketDataProperties
import trade_plumber.props.StorageProperties
import trade_plumber.repository.BarRepository
import trade_plumber.testutils.TestBars.BAR_1
import trade_plumber.testutils.TestBars.BAR_2
import trade_plumber.testutils.TestBars.BAR_3
import trade_plumber.testutils.TestErrors.httpError
import java.nio.file.Path
import java.time.Clock
import java.time.Duration
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

    private val provider = mockk<MarketDataProvider>()
    private lateinit var baseDir: Path
    private lateinit var repository: BarRepository
    private lateinit var manager: MarketDataManager

    @BeforeEach
    fun setUp() {
        baseDir = tempDir.resolve(DATA_DIR)
        repository = BarRepository(StorageProperties(baseDir))
        manager = MarketDataManager(
            provider = provider,
            repository = repository,
            properties = MarketDataProperties(STOCK_HISTORY_START, CRYPTO_HISTORY_START),
            clock = Clock.fixed(NOW, ZoneOffset.UTC),
            retry = RetryConfig.pageRetryTemplate(Duration.ZERO),
        )
    }

    @Test
    fun `update with no file writes the bars from every page in order`() {
        givenStockPages()

        manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

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
        givenStockPages()

        manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        verifySequence {
            provider.barsPage(any(), any(), any(), any(), any(), null)
            provider.barsPage(any(), any(), any(), any(), any(), PAGE_1_TOKEN)
        }
    }

    @Test
    fun `update requests stocks from the stock history start up to now`() {
        givenStockPages()

        manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        verify(exactly = 2) {
            provider.barsPage(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, STOCK_HISTORY_START, NOW, any())
        }
    }

    @Test
    fun `update requests crypto from the crypto history start`() {
        every {
            provider.barsPage(CRYPTO_SYMBOL, AssetClass.CRYPTO, Timeframe.DAY, any(), NOW, null)
        } returns PAGE_1
        every {
            provider.barsPage(CRYPTO_SYMBOL, AssetClass.CRYPTO, Timeframe.DAY, any(), NOW, PAGE_1_TOKEN)
        } returns PAGE_2

        manager.update(CRYPTO_SYMBOL, AssetClass.CRYPTO, Timeframe.DAY)

        verify(exactly = 2) {
            provider.barsPage(CRYPTO_SYMBOL, AssetClass.CRYPTO, Timeframe.DAY, CRYPTO_HISTORY_START, NOW, any())
        }
    }

    @Test
    fun `update treats a header only file as no data and writes the full history`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, emptySequence())
        givenStockPages()

        manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        assertEquals(BAR_3, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    @Test
    fun `update with a provider failure on page 2 throws and creates no file`() {
        givenStockPage(null) returns PAGE_1
        givenStockPage(PAGE_1_TOKEN) throws httpError(HttpStatus.BAD_REQUEST)

        assertFailsWith<AlpacaApiFailedException> {
            manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)
        }

        assertFalse(stockFile().exists())
    }

    @Test
    fun `update with no bars from the provider writes a header only file`() {
        givenStockPage(null) returns BarsPage(emptyList(), null)

        manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        assertEquals("time,open,high,low,close,volume\n", stockFile().readText())
    }

    private fun givenStockPages() {
        givenStockPage(null) returns PAGE_1
        givenStockPage(PAGE_1_TOKEN) returns PAGE_2
    }

    private fun givenStockPage(pageToken: String?) =
        every { provider.barsPage(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, any(), any(), pageToken) }

    private fun stockFile() = baseDir.resolve("stocks/AAPL_DAY.csv")

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
