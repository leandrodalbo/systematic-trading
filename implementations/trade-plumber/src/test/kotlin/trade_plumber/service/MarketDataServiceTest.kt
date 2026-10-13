package trade_plumber.service

import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import trade_plumber.manager.MarketDataManager
import trade_plumber.model.AssetClass
import trade_plumber.model.Timeframe

class MarketDataServiceTest {

    private val manager = mockk<MarketDataManager>()
    private val service = MarketDataService(manager)

    @Test
    fun `update delegates to the manager`() {
        justRun { manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY) }

        service.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY)

        verify(exactly = 1) { manager.update(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY) }
    }

    private companion object {
        const val STOCK_SYMBOL = "AAPL"
    }
}
