import math

import pandas as pd
import pytest

from data_analysis.domain.market import Since
from data_analysis.indicators.performance import move


def on_dates(closes_by_date):
    return pd.Series(list(closes_by_date.values()), index=pd.to_datetime(list(closes_by_date)), dtype=float)


def test_move_since_close():
    closes = on_dates({"2026-11-17": 100, "2026-11-20": 95, "2026-11-24": 110})

    assert move(closes, Since.CLOSE, days=7).iloc[-1] == pytest.approx(10.0)


def test_move_since_close_before_a_gap():
    # 7 days before Mon 23 Nov is Mon 16 Nov, missing here: use the close before it (Fri 13)
    closes = on_dates({"2026-11-13": 100, "2026-11-23": 120})

    assert move(closes, Since.CLOSE, days=7).iloc[-1] == pytest.approx(20.0)


def test_move_needs_enough_history():
    closes = on_dates({"2026-11-23": 100, "2026-11-24": 105})

    assert math.isnan(move(closes, Since.CLOSE, days=7).iloc[-1])


def test_move_from_high():
    closes = on_dates({"2026-01-05": 10, "2026-01-06": 20, "2026-01-07": 15, "2026-01-08": 25, "2026-01-09": 20})

    assert list(move(closes, Since.HIGH, days=365)) == pytest.approx([0.0, 0.0, -25.0, 0.0, -20.0])


def test_move_from_high_forgets_old_highs():
    closes = on_dates({"2026-01-01": 200, "2026-01-10": 100, "2026-01-12": 90})

    assert move(closes, Since.HIGH, days=7).iloc[-1] == pytest.approx(-10.0)


def test_move_from_low():
    closes = on_dates({"2026-01-05": 10, "2026-01-06": 5, "2026-01-07": 8})

    assert move(closes, Since.LOW, days=30).iloc[-1] == pytest.approx(60.0)
