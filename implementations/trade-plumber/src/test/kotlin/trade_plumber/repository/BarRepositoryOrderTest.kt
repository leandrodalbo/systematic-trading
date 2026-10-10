package trade_plumber.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import trade_plumber.model.AssetClass
import trade_plumber.model.Timeframe
import trade_plumber.props.StorageProperties
import trade_plumber.testutils.TestBars.BAR_1
import trade_plumber.testutils.TestBars.BAR_2
import trade_plumber.testutils.TestBars.BAR_2_REVISED
import trade_plumber.testutils.TestBars.BAR_3
import java.math.BigDecimal
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class BarRepositoryOrderTest {

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
    fun `write rejects bars out of order and keeps the original file`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_2))
        val original = stockFile().readText()

        assertFailsWith<IllegalArgumentException> {
            repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_3, BAR_2))
        }

        assertEquals(original, stockFile().readText())
        assertFalse(tempFile().exists())
    }

    @Test
    fun `write rejects a duplicate time`() {
        assertFailsWith<IllegalArgumentException> {
            repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_2, BAR_2_REVISED))
        }
    }

    @Test
    fun `write out of order with no existing file creates nothing`() {
        assertFailsWith<IllegalArgumentException> {
            repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_2, BAR_1))
        }

        assertFalse(stockFile().exists())
        assertFalse(tempFile().exists())
    }

    @Test
    fun `replace last and append rejects new bars out of order and keeps the original file`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_2))
        val original = stockFile().readText()

        assertFailsWith<IllegalArgumentException> {
            repository.replaceLastAndAppend(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_3, BAR_2_REVISED))
        }

        assertEquals(original, stockFile().readText())
        assertFalse(tempFile().exists())
    }

    @Test
    fun `replace last and append rejects a first bar with the same time as the last kept row`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_2))
        val original = stockFile().readText()
        val sameTimeAsKept = BAR_1.copy(close = BigDecimal("186.00"))

        assertFailsWith<IllegalArgumentException> {
            repository.replaceLastAndAppend(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(sameTimeAsKept))
        }

        assertEquals(original, stockFile().readText())
        assertFalse(tempFile().exists())
    }

    @Test
    fun `replace last and append rejects a first bar older than the last kept row`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_2, BAR_3))

        assertFailsWith<IllegalArgumentException> {
            repository.replaceLastAndAppend(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1))
        }
    }

    @Test
    fun `replace last and append on a header only file accepts any ascending bars`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, emptySequence())

        repository.replaceLastAndAppend(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_2))

        assertEquals(BAR_2, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    @Test
    fun `order error message names both times`() {
        val ex = assertFailsWith<IllegalArgumentException> {
            repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_2, BAR_1))
        }

        val message = ex.message.orEmpty()
        assertContains(message, BAR_1.time.toString())
        assertContains(message, BAR_2.time.toString())
    }

    private fun stockFile() = baseDir.resolve("stocks/AAPL_DAY.csv")

    private fun tempFile() = baseDir.resolve("stocks/AAPL_DAY.csv.tmp")

    private companion object {
        const val DATA_DIR = "data"
        const val STOCK_SYMBOL = "AAPL"
    }
}
