from dataclasses import dataclass

import pandas as pd

from data_analysis.domain.market import BarColumn
from data_analysis.domain.trade import ClosedTrade, Exit, OpenTrade, SystemTradeSetup


@dataclass(frozen=True)
class _Holding:
    trade: OpenTrade
    quantity: float
    entry_number: int

    def days_held(self, number: int) -> int:
        """Bars since the entry bar (day 0), at bar `number`."""
        return number - self.entry_number


def run_backtest(
    bars: pd.DataFrame,
    entries: pd.Series,
    setup: SystemTradeSetup,
    ticker: str,
    start: pd.Timestamp,
    end: pd.Timestamp,
    initial_equity: float,
    trade_size: float,
) -> tuple[list[ClosedTrade], pd.Series]:
    """Walks the bars from `start` to `end`, one trade at most: each bar checks the exit first, then the entry.

    Returns the closed trades and the equity at each close. A trade still open on `end` stays open:
    it is not in the trades, but the equity counts it at the last close.
    """
    trades: list[ClosedTrade] = []
    holding: _Holding | None = None
    equity: dict[pd.Timestamp, float] = {}
    for number, (date, bar) in enumerate(bars.loc[start:end].iterrows()):
        sold = _exit(holding, date, bar, number)
        if sold is not None:
            trades.append(
                ClosedTrade(
                    open_trade=holding.trade,
                    exit=sold,
                    quantity=holding.quantity,
                    days_held=holding.days_held(number),
                )
            )
            holding = None
        if holding is None and entries[date]:
            close = bar[BarColumn.CLOSE]
            trade = OpenTrade.from_entry(ticker, date, close, setup)
            holding = _Holding(trade=trade, quantity=trade_size / close, entry_number=number)
        equity[date] = initial_equity + sum(trade.profit for trade in trades) + _open_profit(holding, bar)
    return trades, pd.Series(equity)


def _exit(holding: _Holding | None, date: pd.Timestamp, bar: pd.Series, number: int) -> Exit | None:
    """`None` when nothing is held or nothing was hit."""
    if holding is None:
        return None
    return holding.trade.exit_on(date, bar, days_held=holding.days_held(number))


def _open_profit(holding: _Holding | None, bar: pd.Series) -> float:
    """The open trade marked at this bar's close; 0 when nothing is held."""
    if holding is None:
        return 0
    return holding.trade.profit_at(bar[BarColumn.CLOSE], holding.quantity)
