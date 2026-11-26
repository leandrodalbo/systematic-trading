import pytest

from data_analysis.domain.summary import Summary


def test_no_trades(summary_of, daily):
    flat = daily([100_000, 100_000, 100_000])

    summary = summary_of(trades=[], equity=flat)

    assert summary == Summary(
        trade_count=0,
        winner_count=0,
        loser_count=0,
        average_profit=None,
        average_profit_percent=None,
        winners_total=0,
        winners_average=None,
        winners_average_percent=None,
        largest_win=None,
        losers_total=0,
        losers_average=None,
        losers_average_percent=None,
        largest_loss=None,
        ending_capital=100_000,
        net_profit=0,
        net_profit_percent=0,
        exposure_percent=0,
        risk_adjusted_return_percent=None,
        annual_return_percent=0,
        max_drawdown_percent=0,
        car_mdd=None,
    )


def test_trade_stats(summary_of, daily, three_trades):
    summary = summary_of(trades=three_trades, equity=daily([100_000] * 10))

    assert (summary.trade_count, summary.winner_count, summary.loser_count) == (3, 2, 1)
    assert summary.average_profit == 100
    assert summary.average_profit_percent == pytest.approx(1)
    assert summary.winners_total == 400
    assert summary.winners_average == 200
    assert summary.winners_average_percent == pytest.approx(2)
    assert summary.largest_win == 300
    assert summary.losers_total == -100
    assert summary.losers_average == -100
    assert summary.losers_average_percent == pytest.approx(-1)
    assert summary.largest_loss == -100


def test_zero_profit_is_loser(summary_of, daily, closed_trade):
    summary = summary_of(trades=[closed_trade(100, days_held=1)], equity=daily([100_000, 100_000]))

    assert (summary.winner_count, summary.loser_count) == (0, 1)
    assert summary.largest_loss == 0


def test_no_losers(summary_of, daily, closed_trade):
    summary = summary_of(trades=[closed_trade(103, days_held=1)], equity=daily([100_000, 100_300]))

    assert summary.losers_total == 0
    assert summary.losers_average is None
    assert summary.losers_average_percent is None
    assert summary.largest_loss is None


def test_no_winners(summary_of, daily, closed_trade):
    summary = summary_of(trades=[closed_trade(99, days_held=1)], equity=daily([100_000, 99_900]))

    assert summary.winners_total == 0
    assert summary.winners_average is None
    assert summary.winners_average_percent is None
    assert summary.largest_win is None


def test_net_profit(summary_of, daily):
    summary = summary_of(trades=[], equity=daily([100_000, 100_200, 100_400]))

    assert summary.ending_capital == 100_400
    assert summary.net_profit == 400
    assert summary.net_profit_percent == pytest.approx(0.4)


def test_exposure(summary_of, daily, three_trades):
    summary = summary_of(trades=three_trades, equity=daily([100_000] * 9 + [100_400]))

    assert summary.exposure_percent == pytest.approx(40)
    assert summary.risk_adjusted_return_percent == pytest.approx(0.01)


def test_annual_return(summary_of, equity_over):
    four_years = equity_over(days=1461, first=100_000, last=146_410)

    summary = summary_of(trades=[], equity=four_years)

    assert summary.annual_return_percent == pytest.approx(10)
    assert summary.max_drawdown_percent == 0
    assert summary.car_mdd is None


def test_max_drawdown(summary_of, daily):
    summary = summary_of(trades=[], equity=daily([100_000, 120_000, 90_000, 130_000]))

    assert summary.max_drawdown_percent == pytest.approx(25)
    assert summary.car_mdd == pytest.approx(summary.annual_return_percent / 25)
