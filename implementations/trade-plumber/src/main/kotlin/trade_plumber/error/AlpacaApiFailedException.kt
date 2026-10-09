package trade_plumber.error

class AlpacaApiFailedException(val symbol: String) :
    RuntimeException("${ErrorMessage.ALPACA_FAILED.message} for $symbol")
