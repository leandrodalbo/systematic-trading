import pandas as pd
import pytest

from data_analysis.backtest.run import run_backtest


@pytest.fixture
def bars():
    """Builds daily bars from (open, high, low, close) rows, one per calendar day from 2026-01-05."""
    def build(rows):
        dates = pd.date_range("2026-01-05", periods=len(rows), freq="D")
        return pd.DataFrame(rows, index=dates, columns=["open", "high", "low", "close"], dtype=float)
    return build


@pytest.fixture
def backtest(setup):
    """Runs SPY with 100,000 initial equity and 10,000 per trade; start and end default to the first and last bar."""
    def run(bars, entries, start=None, end=None):
        return run_backtest(
            bars=bars,
            entries=pd.Series(entries, index=bars.index),
            setup=setup,
            ticker="SPY",
            start=start if start is not None else bars.index[0],
            end=end if end is not None else bars.index[-1],
            initial_equity=100_000,
            trade_size=10_000,
        )
    return run
