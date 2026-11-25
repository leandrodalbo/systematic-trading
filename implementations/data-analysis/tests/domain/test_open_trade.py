from dataclasses import replace

import pytest

from data_analysis.domain.trade import OpenTrade


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
