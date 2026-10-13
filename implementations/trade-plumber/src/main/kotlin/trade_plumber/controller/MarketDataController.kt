package trade_plumber.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import trade_plumber.requests.UpdateRequest
import trade_plumber.service.MarketDataService

@RestController
@RequestMapping(MarketDataController.BASE_PATH)
class MarketDataController(
    private val service: MarketDataService,
) {

    @PostMapping(UPDATES_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@RequestBody request: UpdateRequest) =
        service.update(request.symbol, request.assetClass, request.timeframe)

    companion object {
        const val BASE_PATH = "/market-data"
        const val UPDATES_PATH = "/updates"
    }
}
