package trade_plumber.repository

import org.springframework.stereotype.Repository
import trade_plumber.model.AssetClass
import trade_plumber.model.Bar
import trade_plumber.model.Timeframe
import trade_plumber.props.StorageProperties
import java.math.BigDecimal
import java.nio.file.Path
import java.time.Instant
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readLines
import kotlin.io.path.writeText

@Repository
class BarRepository(private val properties: StorageProperties) {

    fun lastBar(symbol: String, assetClass: AssetClass, timeframe: Timeframe): Bar? {
        val file = fileFor(symbol, assetClass, timeframe)
        if (!file.exists()) return null

        return file.readLines()
            .drop(HEADER_LINES)
            .lastOrNull { it.isNotBlank() }
            ?.let(::toBar)
    }

    fun write(symbol: String, assetClass: AssetClass, timeframe: Timeframe, bars: List<Bar>) {
        val file = fileFor(symbol, assetClass, timeframe)
        file.parent.createDirectories()
        file.writeText(buildString {
            append(HEADER).append(NEW_LINE)
            bars.forEach { append(toRow(it)).append(NEW_LINE) }
        })
    }

    private fun fileFor(symbol: String, assetClass: AssetClass, timeframe: Timeframe): Path =
        properties.baseDir
            .resolve(folderFor(assetClass))
            .resolve("${symbol.replace(SYMBOL_SEPARATOR, FILE_SYMBOL_SEPARATOR)}$NAME_SEPARATOR${timeframe.name}$CSV_EXTENSION")

    private fun folderFor(assetClass: AssetClass) = when (assetClass) {
        AssetClass.STOCK -> STOCKS_FOLDER
        AssetClass.CRYPTO -> CRYPTO_FOLDER
    }

    private fun toRow(bar: Bar) = listOf(
        bar.time.toString(),
        bar.open.toPlainString(),
        bar.high.toPlainString(),
        bar.low.toPlainString(),
        bar.close.toPlainString(),
        bar.volume.toPlainString(),
    ).joinToString(COLUMN_SEPARATOR)

    private fun toBar(row: String): Bar {
        val columns = row.split(COLUMN_SEPARATOR)
        return Bar(
            time = Instant.parse(columns[TIME_COLUMN]),
            open = BigDecimal(columns[OPEN_COLUMN]),
            high = BigDecimal(columns[HIGH_COLUMN]),
            low = BigDecimal(columns[LOW_COLUMN]),
            close = BigDecimal(columns[CLOSE_COLUMN]),
            volume = BigDecimal(columns[VOLUME_COLUMN]),
        )
    }

    private companion object {
        const val STOCKS_FOLDER = "stocks"
        const val CRYPTO_FOLDER = "crypto"
        const val CSV_EXTENSION = ".csv"
        const val NAME_SEPARATOR = "_"
        const val SYMBOL_SEPARATOR = "/"
        const val FILE_SYMBOL_SEPARATOR = "-"

        const val HEADER = "time,open,high,low,close,volume"
        const val HEADER_LINES = 1
        const val COLUMN_SEPARATOR = ","
        const val NEW_LINE = "\n"

        const val TIME_COLUMN = 0
        const val OPEN_COLUMN = 1
        const val HIGH_COLUMN = 2
        const val LOW_COLUMN = 3
        const val CLOSE_COLUMN = 4
        const val VOLUME_COLUMN = 5
    }
}
