import pandas as pd
import pytest

from data_analysis.domain.summary import Summary
from data_analysis.domain.trade import ClosedTrade, Exit, OpenTrade


@pytest.fixture
def closed_trade(setup):
    """Builds a closed SPY trade: quantity 100 bought at 100 (10,000), sold at `exit_price` after `days_held` bars."""
    def build(exit_price, days_held):
        entry_date = pd.Timestamp("2026-01-05")
        return ClosedTrade(
            open_trade=OpenTrade.from_entry("SPY", entry_date, 100, setup),
            exit=Exit.at_close(entry_date + pd.Timedelta(days=days_held), exit_price),
            quantity=100,
            days_held=days_held,
        )
    return build


@pytest.fixture
def three_trades(closed_trade):
    """+300 (3%) held 2 bars, -100 (-1%) held 1 bar, +100 (1%) held 1 bar."""
    return [
        closed_trade(103, days_held=2),
        closed_trade(99, days_held=1),
        closed_trade(101, days_held=1),
    ]


@pytest.fixture
def equity_over():
    """Builds a two-point equity: `first` on 2026-01-01 and `last` `days` calendar days later."""
    def build(days, first, last):
        start = pd.Timestamp("2026-01-01")
        return pd.Series([first, last], index=[start, start + pd.Timedelta(days=days)], dtype=float)
    return build


@pytest.fixture
def summary_of():
    """Summarises a run that started with 100,000."""
    def run(trades, equity):
        return Summary.from_run(trades=trades, equity=equity, initial_equity=100_000)
    return run
