package trade_plumber.error

enum class ErrorMessage(val message: String) {
    ALPACA_HTTP_ERROR("Alpaca returned an error status"),
    ALPACA_UNREACHABLE("Alpaca could not be reached"),
    ALPACA_EMPTY_RESPONSE("Empty response from Alpaca"),
}
