import pandas as pd

from data_analysis.core.constants import PERCENT
from data_analysis.domain.cross import Cross, LastCross


def vs_average(closes: pd.Series, window: int) -> pd.Series:
    return (closes / _average(closes, window) - 1) * PERCENT


def average_gap(closes: pd.Series, fast: int, slow: int) -> pd.Series:
    return (_average(closes, fast) / _average(closes, slow) - 1) * PERCENT


def last_cross(closes: pd.Series, fast: int, slow: int) -> LastCross | None:
    flips = _flip_days(_fast_above(closes, fast, slow))
    if flips.empty:
        return None
    kind = Cross.GOLDEN if flips.iloc[-1] else Cross.DEATH
    return LastCross(date=flips.index[-1], kind=kind)


def _average(closes: pd.Series, window: int) -> pd.Series:
    return closes.rolling(window).mean()


def _flip_days(fast_above: pd.Series) -> pd.Series:
    """Days the fast average moved to the other side. The first ready day is a start, not a cross."""
    changed = fast_above != fast_above.shift()
    return fast_above[changed].iloc[1:]


def _fast_above(closes: pd.Series, fast: int, slow: int) -> pd.Series:
    slow_average = _average(closes, slow)
    both_ready = slow_average.notna()
    return (_average(closes, fast) > slow_average)[both_ready]
