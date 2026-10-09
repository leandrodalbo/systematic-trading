package trade_plumber.conf

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import trade_plumber.dataprovider.AlpacaMarketDataProvider
import trade_plumber.dataprovider.MarketDataProvider
import trade_plumber.props.AlpacaProperties

@Configuration
class AlpacaConfig(private val properties: AlpacaProperties) {

    @Bean
    fun alpacaMarketDataProvider(builder: RestClient.Builder): MarketDataProvider =
        AlpacaMarketDataProvider(
            builder
                .baseUrl(properties.dataUrl)
                .defaultHeader(ALPACA_KEY_HEADER, properties.keyId)
                .defaultHeader(ALPACA_SECRET_HEADER, properties.secretKey)
                .build()
        )

    private companion object {
        const val ALPACA_KEY_HEADER = "APCA-API-KEY-ID"
        const val ALPACA_SECRET_HEADER = "APCA-API-SECRET-KEY"
    }
}
