package trade_plumber.model

data class BarsPage(
    val bars: List<Bar>,
    val nextPageToken: String?,
)
