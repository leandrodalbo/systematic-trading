from dataclasses import replace

import pytest

from data_analysis.domain.trade import Exit, ExitReason, OpenTrade


def test_open_trade(trade, setup):
    opened = OpenTrade.from_entry(ticker=trade.ticker, entry_date=trade.entry_date, entry_close=100, setup=setup)

    assert opened == trade


def test_zero_stop_at_entry(trade, setup):
    no_room = replace(setup, stop_percent=0)

    opened = OpenTrade.from_entry(ticker=trade.ticker, entry_date=trade.entry_date, entry_close=100, setup=no_room)

    assert opened.stop_price == pytest.approx(100)


def test_stop_reached(trade):
    assert trade.stop_reached(low=98)
    assert trade.stop_reached(low=97)
    assert not trade.stop_reached(low=98.01)


def test_target_reached(trade):
    assert trade.target_reached(high=105)
    assert trade.target_reached(high=106)
    assert not trade.target_reached(high=104.99)


def test_holding_days_reached(trade):
    assert trade.holding_days_reached(days_held=7)
    assert trade.holding_days_reached(days_held=8)
    assert not trade.holding_days_reached(days_held=6)


def test_no_exit(trade, bar, exit_date):
    assert trade.exit_on(exit_date, bar(open=100, high=101, low=99, close=100), days_held=3) is None
    assert trade.exit_on(exit_date, bar(open=100, high=104.99, low=98.01, close=104), days_held=6) is None


def test_exit_at_stop(trade, bar, exit_date):
    at_stop = Exit(date=exit_date, price=98, reason=ExitReason.STOP)
    gap_down = Exit(date=exit_date, price=96, reason=ExitReason.STOP)

    assert trade.exit_on(exit_date, bar(open=99, high=100, low=97, close=99), days_held=3) == at_stop
    assert trade.exit_on(exit_date, bar(open=99, high=100, low=98, close=99), days_held=3) == at_stop
    assert trade.exit_on(exit_date, bar(open=96, high=97, low=95, close=96), days_held=3) == gap_down


def test_exit_at_target(trade, bar, exit_date):
    at_target = Exit(date=exit_date, price=105, reason=ExitReason.TARGET)

    assert trade.exit_on(exit_date, bar(open=101, high=106, low=100, close=104), days_held=3) == at_target
    assert trade.exit_on(exit_date, bar(open=101, high=105, low=100, close=104), days_held=3) == at_target
    assert trade.exit_on(exit_date, bar(open=107, high=108, low=106, close=107), days_held=3) == at_target


def test_exit_at_close(trade, bar, exit_date):
    at_close = Exit(date=exit_date, price=102, reason=ExitReason.HOLDING_DAYS)

    assert trade.exit_on(exit_date, bar(open=101, high=103, low=100, close=102), days_held=7) == at_close
    assert trade.exit_on(exit_date, bar(open=101, high=103, low=100, close=102), days_held=8) == at_close


def test_exit_order(trade, bar, exit_date):
    at_stop = Exit(date=exit_date, price=98, reason=ExitReason.STOP)
    at_target = Exit(date=exit_date, price=105, reason=ExitReason.TARGET)

    assert trade.exit_on(exit_date, bar(open=100, high=106, low=97, close=100), days_held=3) == at_stop
    assert trade.exit_on(exit_date, bar(open=101, high=106, low=100, close=104), days_held=7) == at_target
    assert trade.exit_on(exit_date, bar(open=99, high=100, low=97, close=99), days_held=7) == at_stop
