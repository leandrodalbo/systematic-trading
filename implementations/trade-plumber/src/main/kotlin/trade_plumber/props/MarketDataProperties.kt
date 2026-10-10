package trade_plumber.props

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Instant

@ConfigurationProperties("market-data")
data class MarketDataProperties(
    val stockHistoryStart: Instant,
    val cryptoHistoryStart: Instant,
)
