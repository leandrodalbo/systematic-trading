import math

import pytest

from data_analysis.domain.market import Cross, LastCross
from data_analysis.indicators.trend import average_gap, last_cross, vs_average

FAST = 2
SLOW = 3
# Average of 2 vs average of 3: above on day 3, below from day 4, above again on day 7.
DIP_AND_RECOVER = [10, 11, 12, 8, 6, 9, 15]


def test_above_average(daily):
    assert vs_average(daily([10, 8, 12]), window=3).iloc[-1] == pytest.approx(20.0)


def test_below_average(daily):
    assert vs_average(daily([10, 12, 8]), window=3).iloc[-1] == pytest.approx(-20.0)


def test_average_needs_full_window(daily):
    result = vs_average(daily([10, 8, 12]), window=3)

    assert math.isnan(result.iloc[0])
    assert math.isnan(result.iloc[1])


def test_average_gap(daily):
    assert average_gap(daily([9, 9, 12]), fast=FAST, slow=SLOW).iloc[-1] == pytest.approx(5.0)


def test_golden_cross(daily):
    closes = daily(DIP_AND_RECOVER)

    assert last_cross(closes, fast=FAST, slow=SLOW) == LastCross(date=closes.index[6], kind=Cross.GOLDEN)


def test_death_cross(daily):
    closes = daily(DIP_AND_RECOVER[:6])

    assert last_cross(closes, fast=FAST, slow=SLOW) == LastCross(date=closes.index[3], kind=Cross.DEATH)


def test_no_cross(daily):
    assert last_cross(daily([1, 2, 3, 4, 5]), fast=FAST, slow=SLOW) is None
