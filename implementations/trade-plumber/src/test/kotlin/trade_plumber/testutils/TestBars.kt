package trade_plumber.testutils

import trade_plumber.model.Bar
import java.math.BigDecimal
import java.time.Instant

object TestBars {
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
    val BAR_2_REVISED = BAR_2.copy(
        close = BigDecimal("184.30"),
        volume = BigDecimal("58500000"),
    )
    val BAR_3 = Bar(
        time = Instant.parse("2024-01-04T05:00:00Z"),
        open = BigDecimal("182.15"),
        high = BigDecimal("183.09"),
        low = BigDecimal("180.88"),
        close = BigDecimal("181.91"),
        volume = BigDecimal("71983600"),
    )
}
