package trade_plumber.model

import java.math.BigDecimal
import java.time.Instant

data class Bar(
    val time: Instant,
    val open: BigDecimal,
    val high: BigDecimal,
    val low: BigDecimal,
    val close: BigDecimal,
    val volume: BigDecimal,
)
