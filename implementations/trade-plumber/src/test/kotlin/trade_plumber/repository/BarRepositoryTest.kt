package trade_plumber.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import trade_plumber.model.AssetClass
import trade_plumber.model.Bar
import trade_plumber.model.Timeframe
import trade_plumber.props.StorageProperties
import java.io.IOException
import java.math.BigDecimal
import java.nio.file.Path
import java.time.Instant
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BarRepositoryTest {

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
    fun `last bar is null when there is no file`() {
        assertNull(repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    @Test
    fun `write creates missing folders and the stock file`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1))

        assertTrue(baseDir.resolve("stocks/AAPL_DAY.csv").exists())
    }

    @Test
    fun `crypto symbol slash becomes a dash in the file name`() {
        repository.write(CRYPTO_SYMBOL, AssetClass.CRYPTO, Timeframe.HOURS4, sequenceOf(BAR_1))

        assertTrue(baseDir.resolve("crypto/BTC-USD_HOURS4.csv").exists())
    }

    @Test
    fun `write stores a header and one row per bar`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_2))

        val expected = """
            time,open,high,low,close,volume
            2024-01-02T05:00:00Z,187.15,188.44,183.885,185.64,82488700
            2024-01-03T05:00:00Z,184.22,185.88,183.43,184.25,58414500.5
        """.trimIndent() + "\n"

        assertEquals(expected, baseDir.resolve("stocks/AAPL_DAY.csv").readText())
    }

    @Test
    fun `numbers in scientific notation are written as plain decimals`() {
        val bar = BAR_1.copy(volume = BigDecimal("8.24887E+7"))

        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(bar))

        val lastLine = baseDir.resolve("stocks/AAPL_DAY.csv").readText().trimEnd().lines().last()
        assertEquals("2024-01-02T05:00:00Z,187.15,188.44,183.885,185.64,82488700", lastLine)
    }

    @Test
    fun `last bar returns the newest bar written`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_2))

        assertEquals(BAR_2, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    @Test
    fun `write overwrites an existing file`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_2))
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1))

        assertEquals(BAR_1, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    @Test
    fun `write leaves no temp file behind`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_2))

        assertFalse(baseDir.resolve("stocks/AAPL_DAY.csv.tmp").exists())
    }

    @Test
    fun `failed write keeps the original file and throws`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_2))
        val file = baseDir.resolve("stocks/AAPL_DAY.csv")
        val original = file.readText()
        baseDir.resolve("stocks/AAPL_DAY.csv.tmp").createDirectory()

        assertFailsWith<IOException> {
            repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1))
        }

        assertEquals(original, file.readText())
    }

    @Test
    fun `failure while streaming bars removes the temp file and keeps the original`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1, BAR_2))
        val file = baseDir.resolve("stocks/AAPL_DAY.csv")
        val original = file.readText()
        val failingMidStream = sequence {
            yield(BAR_1)
            throw IllegalStateException(STREAM_FAILURE)
        }

        val ex = assertFailsWith<IllegalStateException> {
            repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, failingMidStream)
        }

        assertEquals(STREAM_FAILURE, ex.message)
        assertEquals(original, file.readText())
        assertFalse(baseDir.resolve("stocks/AAPL_DAY.csv.tmp").exists())
    }

    @Test
    fun `files are kept apart by symbol, asset class and timeframe`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, sequenceOf(BAR_1))
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.WEEK, sequenceOf(BAR_2))

        assertEquals(BAR_1, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
        assertEquals(BAR_2, repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.WEEK))
        assertNull(repository.lastBar(STOCK_SYMBOL, AssetClass.CRYPTO, Timeframe.DAY))
    }

    @Test
    fun `writing no bars leaves a header only file and no last bar`() {
        repository.write(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY, emptySequence())

        assertEquals("time,open,high,low,close,volume\n", baseDir.resolve("stocks/AAPL_DAY.csv").readText())
        assertNull(repository.lastBar(STOCK_SYMBOL, AssetClass.STOCK, Timeframe.DAY))
    }

    private companion object {
        const val DATA_DIR = "data"
        const val STOCK_SYMBOL = "AAPL"
        const val CRYPTO_SYMBOL = "BTC/USD"
        const val STREAM_FAILURE = "alpaca failed on page 2"

        val BAR_1 = Bar(
            time = Instant.parse("2024-01-02T05:00:00Z"),
            open = BigDecimal("187.15"),
            high = BigDecimal("188.44"),
            low = BigDecimal("183.885"),
            close = BigDecimal("185.64"),
            volume = BigDecimal("82488700"),
        )
        val BAR_2 = Bar(
            time = Instant.parse("2024-01-03T05:00:00Z"),
            open = BigDecimal("184.22"),
            high = BigDecimal("185.88"),
            low = BigDecimal("183.43"),
            close = BigDecimal("184.25"),
            volume = BigDecimal("58414500.5"),
        )
    }
}
