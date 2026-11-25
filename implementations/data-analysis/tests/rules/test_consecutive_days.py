from data_analysis.domain.market_direction import MarketDirection
from data_analysis.rules.consecutive_days import consecutive_days


def test_falling_run(daily):
    result = consecutive_days(daily([10, 9, 8, 7]), days=3, direction=MarketDirection.BEARISH)

    assert result.tolist() == [False, False, False, True]


def test_rising_run(daily):
    result = consecutive_days(daily([10, 11, 12, 13]), days=3, direction=MarketDirection.BULLISH)

    assert result.tolist() == [False, False, False, True]


def test_longer_run_stays_true(daily):
    result = consecutive_days(daily([10, 9, 8, 7, 6]), days=3, direction=MarketDirection.BEARISH)

    assert result.tolist() == [False, False, False, True, True]


def test_flat_day_breaks_falling_run(daily):
    result = consecutive_days(daily([10, 9, 9, 8, 7, 6]), days=3, direction=MarketDirection.BEARISH)

    assert result.tolist() == [False, False, False, False, False, True]


def test_flat_day_breaks_rising_run(daily):
    result = consecutive_days(daily([10, 11, 11, 12, 13, 14]), days=3, direction=MarketDirection.BULLISH)

    assert result.tolist() == [False, False, False, False, False, True]


def test_wrong_direction(daily):
    result = consecutive_days(daily([10, 9, 8, 7]), days=3, direction=MarketDirection.BULLISH)

    assert result.tolist() == [False, False, False, False]


def test_too_few_days(daily):
    result = consecutive_days(daily([10, 9, 8]), days=3, direction=MarketDirection.BEARISH)

    assert result.tolist() == [False, False, False]


def test_keeps_dates(daily):
    closes = daily([10, 9, 8, 7])

    assert consecutive_days(closes, days=3, direction=MarketDirection.BEARISH).index.equals(closes.index)
