import pandas as pd
import pytest


@pytest.fixture
def daily():
    """Builds a close series, one value per calendar day."""
    def build(values):
        return pd.Series(values, index=pd.date_range("2026-01-05", periods=len(values), freq="D"), dtype=float)
    return build
