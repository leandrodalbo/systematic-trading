package trade_plumber.error

enum class ErrorMessage(val message: String) {
    ALPACA_HTTP_ERROR("Alpaca returned an error status"),
    ALPACA_UNREACHABLE("Alpaca could not be reached"),
    ALPACA_EMPTY_RESPONSE("Empty response from Alpaca"),
    UPDATE_IN_PROGRESS("A market data update is already in progress"),
    BATCH_UPDATE_STOPPED("Batch update stopped at"),
}
