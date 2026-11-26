from dataclasses import dataclass
from enum import Enum
from typing import Self

import pandas as pd

from data_analysis.core.constants import PERCENT
from data_analysis.domain.market import BarColumn


@dataclass(frozen=True)
class SystemTradeSetup:
    system: str
    stop_percent: float
    target_percent: float
    holding_days: int


class ExitReason(Enum):
    STOP = "stop"
    TARGET = "target"
    HOLDING_DAYS = "holding_days"


@dataclass(frozen=True)
class Exit:
    date: pd.Timestamp
    price: float
    reason: ExitReason

    @classmethod
    def at_stop(cls, date: pd.Timestamp, open_price: float, stop_price: float) -> Self:
        """The stop is a market order: on a gap down it fills at the open, below the stop."""
        return cls(date=date, price=min(open_price, stop_price), reason=ExitReason.STOP)

    @classmethod
    def at_target(cls, date: pd.Timestamp, target_price: float) -> Self:
        """The target fills at its own price, even on a gap up."""
        return cls(date=date, price=target_price, reason=ExitReason.TARGET)

    @classmethod
    def at_close(cls, date: pd.Timestamp, close: float) -> Self:
        """Holding days reached: the trade sells at the bar's close."""
        return cls(date=date, price=close, reason=ExitReason.HOLDING_DAYS)


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

    def exit_on(self, date: pd.Timestamp, bar: pd.Series, days_held: int) -> Exit | None:
        """The stop wins, then the target, then holding days; `None` means nothing was hit, keep the trade."""
        if self.stop_reached(bar[BarColumn.LOW]):
            return Exit.at_stop(date, bar[BarColumn.OPEN], self.stop_price)
        if self.target_reached(bar[BarColumn.HIGH]):
            return Exit.at_target(date, self.target_price)
        if self.holding_days_reached(days_held):
            return Exit.at_close(date, bar[BarColumn.CLOSE])
        return None


def _stop_price(entry_close: float, stop_percent: float) -> float:
    """A `stop_percent` of 2 puts the stop 2% below the entry close."""
    return entry_close * (1 - stop_percent / PERCENT)


def _target_price(entry_close: float, target_percent: float) -> float:
    """A `target_percent` of 5 puts the target 5% above the entry close."""
    return entry_close * (1 + target_percent / PERCENT)
