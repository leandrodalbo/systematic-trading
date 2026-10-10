package trade_plumber.testutils

import org.springframework.http.HttpStatus
import trade_plumber.error.AlpacaApiFailedException
import trade_plumber.error.ErrorMessage

object TestErrors {
    private const val SYMBOL = "AAPL"

    fun unreachable() = AlpacaApiFailedException(SYMBOL, ErrorMessage.ALPACA_UNREACHABLE)

    fun httpError(status: HttpStatus) = AlpacaApiFailedException(SYMBOL, ErrorMessage.ALPACA_HTTP_ERROR, status.value())
}
