package trade_plumber.props

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("alpaca")
data class AlpacaProperties(
    val keyId: String,
    val secretKey: String,
    val dataUrl: String,
)
