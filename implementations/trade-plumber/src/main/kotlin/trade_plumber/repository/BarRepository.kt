package trade_plumber.repository

import org.springframework.stereotype.Repository
import trade_plumber.model.AssetClass
import trade_plumber.model.Bar
import trade_plumber.model.Timeframe
import trade_plumber.props.StorageProperties
import java.io.BufferedReader
import java.io.Writer
import java.math.BigDecimal
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.Instant
import kotlin.io.path.bufferedReader
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.moveTo
import kotlin.io.path.useLines

@Repository
class BarRepository(private val properties: StorageProperties) {

    fun lastBar(symbol: String, assetClass: AssetClass, timeframe: Timeframe): Bar? {
        val file = fileFor(symbol, assetClass, timeframe)
        if (!file.exists()) return null

        return file.useLines { lines ->
            lines.drop(HEADER_LINES)
                .lastOrNull { it.isNotBlank() }
                ?.let(::toBar)
        }
    }

    fun write(symbol: String, assetClass: AssetClass, timeframe: Timeframe, bars: Sequence<Bar>) {
        val file = fileFor(symbol, assetClass, timeframe)
        file.parent.createDirectories()
        writeAtomically(file) { out ->
            out.appendLine(HEADER)
            out.appendInOrder(bars)
        }
    }

    fun replaceLastAndAppend(symbol: String, assetClass: AssetClass, timeframe: Timeframe, bars: Sequence<Bar>) {
        val newBars = bars.iterator()
        if (!newBars.hasNext()) return

        val file = fileFor(symbol, assetClass, timeframe)
        if (!file.exists()) throw NoSuchFileException(file.toString())

        writeAtomically(file) { out ->
            out.appendLine(HEADER)
            val lastKeptTime = file.bufferedReader().use { reader -> copyAllButLastRow(reader, out) }
            out.appendInOrder(newBars.asSequence(), after = lastKeptTime)
        }
    }

    private fun copyAllButLastRow(reader: BufferedReader, out: Writer): Instant? {
        var lastKept: String? = null
        var heldBack: String? = null
        reader.lineSequence()
            .drop(HEADER_LINES)
            .filter { it.isNotBlank() }
            .forEach { row ->
                heldBack?.let {
                    out.appendLine(it)
                    lastKept = it
                }
                heldBack = row
            }
        return lastKept?.let { toBar(it).time }
    }

    private fun Writer.appendInOrder(bars: Sequence<Bar>, after: Instant? = null) =
        inAscendingOrder(bars, after).forEach { appendLine(toRow(it)) }

    private fun inAscendingOrder(bars: Sequence<Bar>, after: Instant? = null): Sequence<Bar> = sequence {
        var previous = after
        bars.forEach { bar ->
            previous?.let { require(bar.time > it) { ORDER_ERROR.format(bar.time, it) } }
            yield(bar)
            previous = bar.time
        }
    }

    private fun writeAtomically(file: Path, writeContent: (Writer) -> Unit) {
        val temp = file.resolveSibling("${file.fileName}$TEMP_EXTENSION")
        runCatching {
            temp.bufferedWriter().use(writeContent)
            temp.moveTo(file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)
        }.onFailure {
            temp.deleteIfExists()
            throw it
        }
    }

    private fun fileFor(symbol: String, assetClass: AssetClass, timeframe: Timeframe): Path =
        properties.baseDir
            .resolve(folderFor(assetClass))
            .resolve(fileNameFor(symbol, timeframe))

    private fun fileNameFor(symbol: String, timeframe: Timeframe): String {
        val fileSymbol = symbol.replace(SYMBOL_SEPARATOR, FILE_SYMBOL_SEPARATOR)
        return "$fileSymbol$NAME_SEPARATOR${timeframe.name}$CSV_EXTENSION"
    }

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
        const val TEMP_EXTENSION = ".tmp"
        const val NAME_SEPARATOR = "_"
        const val SYMBOL_SEPARATOR = "/"
        const val FILE_SYMBOL_SEPARATOR = "-"

        const val HEADER = "time,open,high,low,close,volume"
        const val HEADER_LINES = 1
        const val COLUMN_SEPARATOR = ","
        const val ORDER_ERROR = "bar %s is not after %s"

        const val TIME_COLUMN = 0
        const val OPEN_COLUMN = 1
        const val HIGH_COLUMN = 2
        const val LOW_COLUMN = 3
        const val CLOSE_COLUMN = 4
        const val VOLUME_COLUMN = 5
    }
}
