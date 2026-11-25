from dataclasses import dataclass
from typing import Self

import pandas as pd

from data_analysis.core.constants import PERCENT


@dataclass(frozen=True)
class SystemTradeSetup:
    system: str
    stop_percent: float
    target_percent: float
    holding_days: int


@dataclass(frozen=True)
class OpenTrade:
    ticker: str
    entry_date: pd.Timestamp
    entry_price: float
    stop_price: float
    target_price: float
    setup: SystemTradeSetup

    @classmethod
    def from_entry(cls, ticker: str, entry_date: pd.Timestamp, entry_close: float, setup: SystemTradeSetup) -> Self:
        """The trade opens at the entry bar's close; its stop and target are set from that close."""
        return cls(
            ticker=ticker,
            entry_date=entry_date,
            entry_price=entry_close,
            stop_price=_stop_price(entry_close, setup.stop_percent),
            target_price=_target_price(entry_close, setup.target_percent),
            setup=setup,
        )

    def stop_reached(self, low: float) -> bool:
        """Checked on the low: the stop can be hit during the day, even if the close is above it."""
        return low <= self.stop_price

    def target_reached(self, high: float) -> bool:
        """Checked on the high: the target can be hit during the day, even if the close is below it."""
        return high >= self.target_price

    def holding_days_reached(self, days_held: int) -> bool:
        """`days_held` counts bars since the entry bar, which is day 0."""
        return days_held >= self.setup.holding_days


def _stop_price(entry_close: float, stop_percent: float) -> float:
    """A `stop_percent` of 2 puts the stop 2% below the entry close."""
    return entry_close * (1 - stop_percent / PERCENT)


def _target_price(entry_close: float, target_percent: float) -> float:
    """A `target_percent` of 5 puts the target 5% above the entry close."""
    return entry_close * (1 + target_percent / PERCENT)
