package trade_plumber.service

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test
import trade_plumber.error.BatchUpdateFailedException
import trade_plumber.manager.MarketDataManager
import trade_plumber.model.AssetClass
import trade_plumber.model.Timeframe
import trade_plumber.requests.UpdateListRequest
import trade_plumber.requests.UpdateRequest
import trade_plumber.testutils.TestErrors
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class MarketDataServiceTest {

    private val manager = mockk<MarketDataManager>()
    private val service = MarketDataService(manager)

    @Test
    fun `update delegates to the manager`() {
        justRun { manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY) }

        service.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        verify(exactly = 1) { manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY) }
    }

    @Test
    fun `updateAll updates every request in order`() {
        justRun { manager.update(any(), any(), any()) }

        service.updateAll(BATCH)

        verifyOrder {
            manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)
            manager.update(SECOND_STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)
            manager.update(CRYPTO_SYMBOL, AssetClass.CRYPTO, Timeframe.DAY)
        }
    }

    @Test
    fun `updateAll stops at the first failure and says where it stopped`() {
        val cause = TestErrors.unreachable()
        justRun { manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY) }
        every { manager.update(SECOND_STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY) } throws cause

        val failure = assertFailsWith<BatchUpdateFailedException> { service.updateAll(BATCH) }

        assertEquals(SECOND_STOCK_SYMBOL, failure.failedSymbol)
        assertSame(cause, failure.cause)
        verify(exactly = 0) { manager.update(CRYPTO_SYMBOL, any(), any()) }
    }

    private companion object {
        const val STOCK_SYMBOL = "AAPL"
        const val SECOND_STOCK_SYMBOL = "SPY"
        const val CRYPTO_SYMBOL = "BTC/USD"
        val BATCH = UpdateListRequest(
            listOf(
                UpdateRequest(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY),
                UpdateRequest(SECOND_STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY),
                UpdateRequest(CRYPTO_SYMBOL, AssetClass.CRYPTO, Timeframe.DAY),
            ),
        )
    }
}
