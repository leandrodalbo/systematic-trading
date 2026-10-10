package trade_plumber.conf

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.retry.RetryOperations
import org.springframework.core.retry.RetryPolicy
import org.springframework.core.retry.RetryTemplate
import trade_plumber.error.AlpacaApiFailedException
import java.time.Duration

@Configuration
class RetryConfig {

    @Bean
    fun pageRetry(): RetryOperations = pageRetryTemplate(Duration.ofMillis(INITIAL_DELAY_MILLIS))

    companion object {
        private const val MAX_RETRIES = 3L
        private const val INITIAL_DELAY_MILLIS = 1_000L
        private const val DELAY_MULTIPLIER = 2.0
        private const val MAX_DELAY_MILLIS = 10_000L

        fun pageRetryTemplate(initialDelay: Duration): RetryOperations =
            RetryTemplate(
                RetryPolicy.builder()
                    .maxRetries(MAX_RETRIES)
                    .delay(initialDelay)
                    .multiplier(DELAY_MULTIPLIER)
                    .maxDelay(Duration.ofMillis(MAX_DELAY_MILLIS))
                    .predicate { it is AlpacaApiFailedException && it.isRetryable() }
                    .build()
            )
    }
}
