import pandas as pd

from data_analysis.core.constants import PERCENT
from data_analysis.domain.market import Since


def move(closes: pd.Series, since: Since, days: int) -> pd.Series:
    """% move of each close vs a reference in its last `days`.

    `days` are calendar days, so 7 is one week for stocks and BTC alike. The windows in
    `trend` and `mean_reversion` count rows (trading days) instead.
    """
    return (closes / _reference(closes, since, pd.Timedelta(days=days)) - 1) * PERCENT


def _reference(closes: pd.Series, since: Since, period: pd.Timedelta) -> pd.Series:
    match since:
        case Since.CLOSE:
            return _close_days_ago(closes, period)
        case Since.HIGH:
            return closes.rolling(period).max()
        case Since.LOW:
            return closes.rolling(period).min()


def _close_days_ago(closes: pd.Series, period: pd.Timedelta) -> pd.Series:
    """The close `period` before each day, or the last close before that if it's missing."""
    earlier_dates = closes.index - period
    return closes.reindex(earlier_dates, method="ffill").set_axis(closes.index)
