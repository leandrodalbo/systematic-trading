package trade_plumber.manager

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.http.HttpStatus
import trade_plumber.dataprovider.MarketDataProvider
import trade_plumber.conf.RetryConfig
import trade_plumber.error.AlpacaApiFailedException
import trade_plumber.error.ErrorMessage
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
import trade_plumber.testutils.TestErrors.unreachable
import java.nio.file.Path
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset
import kotlin.io.path.exists
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame

class MarketDataManagerRetryTest {

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
    fun `page 1 unreachable once is retried and the full history is written`() {
        givenPage(null) throws unreachable() andThen PAGE_1
        givenPage(PAGE_TOKEN) returns PAGE_2

        manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        verify(exactly = 2) {
            provider.barsPage(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, STOCK_HISTORY_START, NOW, null)
        }
        assertEquals(BAR_3, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    @Test
    fun `page 2 rate limited once is retried with the same token and page 1 is not fetched again`() {
        givenPage(null) returns PAGE_1
        givenPage(PAGE_TOKEN) throws httpError(HttpStatus.TOO_MANY_REQUESTS) andThen PAGE_2

        manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        verify(exactly = 1) { provider.barsPage(any(), any(), any(), any(), any(), null) }
        verify(exactly = 2) { provider.barsPage(any(), any(), any(), any(), any(), PAGE_TOKEN) }
        assertEquals(BAR_3, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    @Test
    fun `bad request is not retried`() {
        val badRequest = httpError(HttpStatus.BAD_REQUEST)
        givenPage(null) throws badRequest

        val ex = assertFailsWith<AlpacaApiFailedException> {
            manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)
        }

        assertSame(badRequest, ex)
        verify(exactly = 1) { provider.barsPage(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `exception that is not from alpaca is not retried and is rethrown as is`() {
        val unexpected = IllegalStateException(UNEXPECTED_FAILURE)
        givenPage(null) throws unexpected

        val ex = assertFailsWith<IllegalStateException> {
            manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)
        }

        assertSame(unexpected, ex)
        verify(exactly = 1) { provider.barsPage(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `gives up after three retries with the original exception and writes nothing`() {
        givenPage(null) throws unreachable()

        val ex = assertFailsWith<AlpacaApiFailedException> {
            manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)
        }

        assertEquals(ErrorMessage.ALPACA_UNREACHABLE, ex.error)
        verify(exactly = ALL_ATTEMPTS) { provider.barsPage(any(), any(), any(), any(), any(), any()) }
        assertFalse(baseDir.resolve("stocks/AAPL_DAY.csv").exists())
    }

    private fun givenPage(pageToken: String?) =
        every { provider.barsPage(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, STOCK_HISTORY_START, NOW, pageToken) }

    private companion object {
        const val DATA_DIR = "data"
        const val STOCK_SYMBOL = "AAPL"
        const val PAGE_TOKEN = "page-2-please"
        const val UNEXPECTED_FAILURE = "not an alpaca error"
        const val ALL_ATTEMPTS = 4

        val NOW: Instant = Instant.parse("2024-01-05T00:00:00Z")
        val STOCK_HISTORY_START: Instant = Instant.parse("2016-01-01T00:00:00Z")
        val CRYPTO_HISTORY_START: Instant = Instant.parse("2021-01-01T00:00:00Z")

        val PAGE_1 = BarsPage(listOf(BAR_1, BAR_2), PAGE_TOKEN)
        val PAGE_2 = BarsPage(listOf(BAR_3), null)
    }
}
