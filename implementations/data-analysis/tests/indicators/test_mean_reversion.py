import math

import pytest

from data_analysis.indicators.mean_reversion import z_score


def test_z_score_above_mean(daily):
    # mean 2, sample std dev 1
    assert z_score(daily([1, 2, 3]), window=3).iloc[-1] == pytest.approx(1.0)


def test_z_score_below_mean(daily):
    assert z_score(daily([3, 2, 1]), window=3).iloc[-1] == pytest.approx(-1.0)


def test_z_score_needs_full_window(daily):
    assert math.isnan(z_score(daily([1, 2, 3]), window=3).iloc[1])
