package trade_plumber.error

enum class ErrorMessage(val message: String) {
    ALPACA_HTTP_ERROR("Alpaca returned an error status"),
    ALPACA_UNREACHABLE("Alpaca could not be reached"),
    ALPACA_EMPTY_RESPONSE("Empty response from Alpaca"),
    UPDATE_IN_PROGRESS("A market data update is already in progress"),
}

class AlpacaApiFailedException(
    val symbol: String,
    val error: ErrorMessage,
    val status: Int? = null,
    cause: Throwable? = null,
) : RuntimeException("${error.message} for $symbol", cause) {

    fun isRetryable(): Boolean =
         when (error) {
            ErrorMessage.ALPACA_UNREACHABLE -> true
            ErrorMessage.ALPACA_HTTP_ERROR -> isRetryableStatus(status)
            ErrorMessage.ALPACA_EMPTY_RESPONSE -> false
            ErrorMessage.UPDATE_IN_PROGRESS -> false
        }

    private fun isRetryableStatus(status: Int?) =
        status != null && (status == TOO_MANY_REQUESTS || status >= SERVER_ERROR_MIN)

    private companion object {
        const val TOO_MANY_REQUESTS = 429
        const val SERVER_ERROR_MIN = 500
    }
}

class UpdateInProgressException : RuntimeException(ErrorMessage.UPDATE_IN_PROGRESS.message)
