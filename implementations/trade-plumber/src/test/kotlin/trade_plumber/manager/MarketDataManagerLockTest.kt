package trade_plumber.manager

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.http.HttpStatus
import trade_plumber.conf.RetryConfig
import trade_plumber.dataprovider.MarketDataProvider
import trade_plumber.error.AlpacaApiFailedException
import trade_plumber.error.UpdateInProgressException
import trade_plumber.model.AssetClass
import trade_plumber.model.BarsPage
import trade_plumber.model.Timeframe
import trade_plumber.props.MarketDataProperties
import trade_plumber.props.StorageProperties
import trade_plumber.repository.BarRepository
import trade_plumber.testutils.TestBars.BAR_1
import trade_plumber.testutils.TestBars.BAR_2
import trade_plumber.testutils.TestErrors.httpError
import java.nio.file.Path
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class MarketDataManagerLockTest {

    @TempDir
    lateinit var tempDir: Path

    private val provider = mockk<MarketDataProvider>()
    private lateinit var repository: BarRepository
    private lateinit var manager: MarketDataManager

    @BeforeEach
    fun setUp() {
        repository = BarRepository(StorageProperties(tempDir.resolve(DATA_DIR)))
        manager = MarketDataManager(
            provider = provider,
            repository = repository,
            properties = MarketDataProperties(STOCK_HISTORY_START, CRYPTO_HISTORY_START),
            clock = Clock.fixed(NOW, ZoneOffset.UTC),
            retry = RetryConfig.pageRetryTemplate(Duration.ZERO),
        )
    }

    @Test
    fun `second update for the same symbol fails fast while the first is running`() {
        givenStockPage() answers {
            assertFailsWith<UpdateInProgressException> { updateStock() }
            PAGE
        }

        updateStock()

        verify(exactly = 1) { provider.barsPage(any(), any(), any(), any(), any(), any()) }
        assertEquals(BAR_2, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    @Test
    fun `update for another symbol also fails fast while one is running`() {
        givenStockPage() answers {
            assertFailsWith<UpdateInProgressException> {
                manager.update(CRYPTO_SYMBOL, AssetClass.CRYPTO, Timeframe.DAY)
            }
            PAGE
        }

        updateStock()

        verify(exactly = 0) { provider.barsPage(CRYPTO_SYMBOL, any(), any(), any(), any(), any()) }
        assertNull(repository.lastBar(CRYPTO_SYMBOL, AssetClass.CRYPTO, Timeframe.DAY))
    }

    @Test
    fun `a failed update releases the lock so the next update runs`() {
        givenStockPage() throws httpError(HttpStatus.BAD_REQUEST) andThen PAGE

        assertFailsWith<AlpacaApiFailedException> { updateStock() }
        updateStock()

        assertEquals(BAR_2, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    private fun updateStock() = manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

    private fun givenStockPage() =
        every { provider.barsPage(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, STOCK_HISTORY_START, NOW, null) }

    private companion object {
        const val DATA_DIR = "data"
        const val STOCK_SYMBOL = "AAPL"
        const val CRYPTO_SYMBOL = "BTC/USD"

        val NOW: Instant = Instant.parse("2024-01-05T00:00:00Z")
        val STOCK_HISTORY_START: Instant = Instant.parse("2016-01-01T00:00:00Z")
        val CRYPTO_HISTORY_START: Instant = Instant.parse("2021-01-01T00:00:00Z")

        val PAGE = BarsPage(listOf(BAR_1, BAR_2), null)
    }
}
