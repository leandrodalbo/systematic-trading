import pandas as pd

from data_analysis.domain.market_direction import MarketDirection


def consecutive_days(closes: pd.Series, days: int, direction: MarketDirection) -> pd.Series:
    """True on each day whose last `days` closes all moved in `direction` vs the day before.

    A flat day breaks the run. The first `days` days are always False: they have fewer than `days` moves behind them.
    """
    moved = _moved(closes.diff(), direction)
    return moved.rolling(days).sum() == days


def _moved(changes: pd.Series, direction: MarketDirection) -> pd.Series:
    match direction:
        case MarketDirection.BULLISH:
            return changes > 0
        case MarketDirection.BEARISH:
            return changes < 0
