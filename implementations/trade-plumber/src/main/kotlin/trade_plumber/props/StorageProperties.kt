package trade_plumber.props

import org.springframework.boot.context.properties.ConfigurationProperties
import java.nio.file.Path

@ConfigurationProperties("storage")
data class StorageProperties(
    val baseDir: Path,
)
