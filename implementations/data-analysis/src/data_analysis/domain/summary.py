from dataclasses import dataclass
from statistics import fmean
from typing import Self

import pandas as pd

from data_analysis.core.constants import DAYS_PER_YEAR, PERCENT
from data_analysis.domain.trade import ClosedTrade


@dataclass(frozen=True)
class Summary:
    """Every value of one backtest run. `None` means it can't be worked out (nothing to average,
    or a ratio that would divide by zero). Every % is a percent number: 5.0 is 5%."""

    trade_count: int
    winner_count: int
    loser_count: int
    average_profit: float | None
    average_profit_percent: float | None
    winners_total: float
    winners_average: float | None
    winners_average_percent: float | None
    largest_win: float | None
    losers_total: float
    losers_average: float | None
    losers_average_percent: float | None
    largest_loss: float | None
    ending_capital: float
    net_profit: float
    net_profit_percent: float
    exposure_percent: float
    risk_adjusted_return_percent: float | None
    annual_return_percent: float
    max_drawdown_percent: float
    car_mdd: float | None

    @classmethod
    def from_run(cls, trades: list[ClosedTrade], equity: pd.Series, initial_equity: float) -> Self:
        """Every value of one run, from its closed trades and its equity at each close."""
        won, lost = winners(trades), losers(trades)
        net_percent = net_profit_percent(equity, initial_equity)
        exposure = exposure_percent(trades, equity)
        annual_return = annual_return_percent(equity, initial_equity)
        drawdown = max_drawdown_percent(equity)
        return cls(
            trade_count=len(trades),
            winner_count=len(won),
            loser_count=len(lost),
            average_profit=average(trades),
            average_profit_percent=average_percent(trades),
            winners_total=total(won),
            winners_average=average(won),
            winners_average_percent=average_percent(won),
            largest_win=largest_win(won),
            losers_total=total(lost),
            losers_average=average(lost),
            losers_average_percent=average_percent(lost),
            largest_loss=largest_loss(lost),
            ending_capital=ending_capital(equity),
            net_profit=net_profit(equity, initial_equity),
            net_profit_percent=net_percent,
            exposure_percent=exposure,
            risk_adjusted_return_percent=_ratio(net_percent, exposure),
            annual_return_percent=annual_return,
            max_drawdown_percent=drawdown,
            car_mdd=_ratio(annual_return, drawdown),
        )


def winners(trades: list[ClosedTrade]) -> list[ClosedTrade]:
    return [trade for trade in trades if trade.profit > 0]


def losers(trades: list[ClosedTrade]) -> list[ClosedTrade]:
    """A trade with 0 profit is a loser: it didn't win."""
    return [trade for trade in trades if trade.profit <= 0]


def total(trades: list[ClosedTrade]) -> float:
    """0 when there are no trades."""
    return sum(trade.profit for trade in trades)


def average(trades: list[ClosedTrade]) -> float | None:
    """`None` when there are no trades."""
    return _mean([trade.profit for trade in trades])


def average_percent(trades: list[ClosedTrade]) -> float | None:
    """`None` when there are no trades."""
    return _mean([trade.profit_percent for trade in trades])


def largest_win(won: list[ClosedTrade]) -> float | None:
    """The highest profit of the winners; `None` when there are none."""
    return max((trade.profit for trade in won), default=None)


def largest_loss(lost: list[ClosedTrade]) -> float | None:
    """The most negative profit of the losers; `None` when there are none."""
    return min((trade.profit for trade in lost), default=None)


def ending_capital(equity: pd.Series) -> float:
    """The equity at the last close."""
    return equity.iloc[-1]


def net_profit(equity: pd.Series, initial_equity: float) -> float:
    return ending_capital(equity) - initial_equity


def return_percent(equity: pd.Series, initial_equity: float) -> pd.Series:
    """Each day's growth of the account since the start, as a %."""
    return (equity - initial_equity) / initial_equity * PERCENT


def net_profit_percent(equity: pd.Series, initial_equity: float) -> float:
    """The growth on the last day."""
    return return_percent(equity, initial_equity).iloc[-1]


def exposure_percent(trades: list[ClosedTrade], equity: pd.Series) -> float:
    """% of bars that end with a trade held: the entry bar up to the bar before the exit."""
    held = sum(trade.days_held for trade in trades)
    return held / len(equity) * PERCENT


def annual_return_percent(equity: pd.Series, initial_equity: float) -> float:
    """CAR: the yearly growth that turns the initial equity into the ending one, over calendar days."""
    years = (equity.index[-1] - equity.index[0]).days / DAYS_PER_YEAR
    return ((ending_capital(equity) / initial_equity) ** (1 / years) - 1) * PERCENT


def drawdown_percent(equity: pd.Series) -> pd.Series:
    """Each day's fall from the highest equity so far, as a positive %; 0 on a new high."""
    peaks = equity.cummax()
    return (peaks - equity) / peaks * PERCENT


def max_drawdown_percent(equity: pd.Series) -> float:
    """MDD: the largest fall from a peak, as a positive %. Falls inside a trade count."""
    return drawdown_percent(equity).max()


def _mean(values: list[float]) -> float | None:
    """`None` when there is nothing to average."""
    if not values:
        return None
    return fmean(values)


def _ratio(part: float, whole: float) -> float | None:
    """`None` when `whole` is 0: the ratio can't be worked out."""
    if whole == 0:
        return None
    return part / whole
