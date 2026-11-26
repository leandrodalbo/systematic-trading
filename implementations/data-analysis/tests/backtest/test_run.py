import pytest

from data_analysis.domain.trade import ClosedTrade, Exit, ExitReason, OpenTrade


def test_no_trades(backtest, bars):
    flat = bars([(100, 100, 100, 100), (100, 100, 100, 100), (100, 100, 100, 100)])

    trades, equity = backtest(flat, entries=[False, False, False])

    assert trades == []
    assert list(equity) == [100_000, 100_000, 100_000]


def test_exit_at_target(backtest, bars, setup):
    days = bars([(100, 100, 100, 100), (101, 106, 100, 104), (104, 104, 104, 104)])
    entry_day, exit_day = days.index[0], days.index[1]

    trades, equity = backtest(days, entries=[True, False, False])

    assert trades == [
        ClosedTrade(
            open_trade=OpenTrade.from_entry("SPY", entry_day, 100, setup),
            exit=Exit(date=exit_day, price=105, reason=ExitReason.TARGET),
            quantity=100,
            days_held=1,
        )
    ]
    assert trades[0].profit == 500
    assert list(equity) == [100_000, 100_500, 100_500]


def test_open_trade_marked_at_close(backtest, bars):
    days = bars([(100, 100, 100, 100), (100, 103, 99, 103)])

    trades, equity = backtest(days, entries=[True, False])

    assert trades == []
    assert list(equity) == [100_000, 100_300]


def test_buy_again_same_day(backtest, bars):
    days = bars([(100, 100, 100, 100), (99, 100, 97, 99), (99, 100, 99, 100)])

    trades, equity = backtest(days, entries=[True, True, False])

    assert [trade.exit.reason for trade in trades] == [ExitReason.STOP]
    assert list(equity) == pytest.approx([100_000, 99_800, 99_901.0101], abs=0.0001)


def test_buy_above_trade_size(backtest, bars):
    days = bars([(20_000, 20_000, 20_000, 20_000), (20_000, 20_200, 20_000, 20_200)])

    trades, equity = backtest(days, entries=[True, False])

    assert trades == []
    assert list(equity) == [100_000, 100_100]


def test_days_held(backtest, bars):
    days = bars([(100, 100, 100, 100), (100, 101, 99, 100), (99, 100, 97, 99)])

    trades, _ = backtest(days, entries=[True, False, False])

    assert [(trade.exit.reason, trade.days_held) for trade in trades] == [(ExitReason.STOP, 2)]


def test_entry_while_holding(backtest, bars):
    days = bars([(100, 100, 100, 100), (100, 101, 100, 101), (101, 102, 101, 102)])

    trades, equity = backtest(days, entries=[True, True, False])

    assert trades == []
    assert list(equity) == [100_000, 100_100, 100_200]


def test_before_start(backtest, bars):
    days = bars([(100, 100, 100, 100), (100, 101, 100, 101), (101, 102, 101, 102)])

    trades, equity = backtest(days, entries=[True, False, False], start=days.index[1])

    assert trades == []
    assert list(equity.index) == [days.index[1], days.index[2]]
    assert list(equity) == [100_000, 100_000]
