import pandas as pd
import pytest

from data_analysis.domain.trade import OpenTrade, SystemTradeSetup


@pytest.fixture
def daily():
    """Builds a close series, one value per calendar day."""
    def build(values):
        return pd.Series(values, index=pd.date_range("2026-01-05", periods=len(values), freq="D"), dtype=float)
    return build


@pytest.fixture
def setup():
    """A system with a 2% stop, a 5% target and 7 holding days."""
    return SystemTradeSetup(system="naive-sequence", stop_percent=2, target_percent=5, holding_days=7)


@pytest.fixture
def trade(setup):
    """SPY bought at 100: stop 98, target 105, 7 holding days."""
    return OpenTrade(
        ticker="SPY",
        entry_date=pd.Timestamp("2026-01-05"),
        entry_price=100,
        stop_price=98,
        target_price=105,
        setup=setup,
    )
