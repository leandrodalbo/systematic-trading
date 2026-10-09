package trade_plumber

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class TradePlumberApplication

fun main(args: Array<String>) {
	runApplication<TradePlumberApplication>(*args)
}
