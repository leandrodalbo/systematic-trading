"""Bandy's naive system (chapter 3): buy after `DAYS_IN_A_ROW` closes in one direction, on SPY and BTC.

Runs each direction and level combination, prints their summaries side by side and saves a chart
that compares the runs, per ticker.
"""

import argparse
from dataclasses import asdict, dataclass
from pathlib import Path

import matplotlib.pyplot as plt
import pandas as pd

from data_analysis.backtest.run import run_backtest
from data_analysis.data.bars import load_bars
from data_analysis.domain.market import AssetClass, BarColumn, MarketDirection
from data_analysis.domain.summary import Summary, drawdown_percent, return_percent
from data_analysis.domain.trade import SystemTradeSetup
from data_analysis.systems.naive_sequence import consecutive_days

SYSTEM = "naive-sequence-system"
HOLDING_DAYS = 7
TIGHT = SystemTradeSetup(system=SYSTEM, stop_percent=1, target_percent=2, holding_days=HOLDING_DAYS)
WIDE = SystemTradeSetup(system=SYSTEM, stop_percent=3, target_percent=6, holding_days=HOLDING_DAYS)
DAYS_IN_A_ROW = 3
INITIAL_EQUITY = 100_000
TRADE_SIZE = 10_000


@dataclass(frozen=True)
class _Run:
    symbol: str
    asset_class: AssetClass
    direction: MarketDirection
    setup: SystemTradeSetup

    @property
    def levels(self) -> str:
        return f"{self.direction.name.lower()} {self.setup.stop_percent}%/{self.setup.target_percent}%"

    @property
    def label(self) -> str:
        return f"{self.symbol} {self.levels}"


@dataclass(frozen=True)
class _Result:
    run: _Run
    summary: Summary
    equity: pd.Series


RUNS = [
    _Run("SPY", AssetClass.STOCK, MarketDirection.BULLISH, TIGHT),
    _Run("SPY", AssetClass.STOCK, MarketDirection.BEARISH, TIGHT),
    _Run("BTC/USD", AssetClass.CRYPTO, MarketDirection.BULLISH, WIDE),
    _Run("BTC/USD", AssetClass.CRYPTO, MarketDirection.BEARISH, WIDE),
]

PERCENT_SUFFIX = "_percent"
RATIO_FIELDS = {"car_mdd"}
MONEY_FORMAT = "{:,.0f}"
RATIO_FORMAT = "{:.2f}"

CHART_FILE = "chart.png"
FIGURE_SIZE = (13, 7)
HEIGHT_RATIOS = [3, 2]
DPI = 150
LINE_WIDTH = 2
LEGEND_PLACE = "upper left"
BACKGROUND = "#0b0f14"
TEXT = "#e6edf3"
MUTED = "#8b949e"
COLORS = {
    (MarketDirection.BULLISH, TIGHT): "#338ced",
    (MarketDirection.BEARISH, TIGHT): "#e76624",
    (MarketDirection.BULLISH, WIDE): "#00a888",
    (MarketDirection.BEARISH, WIDE): "#d28e05",
}




def main() -> None:
    output_dir = _output_dir()
    bars = {run.symbol: _finished(load_bars(run.symbol, run.asset_class)) for run in RUNS}
    results = [_backtest(run, bars[run.symbol]) for run in RUNS]
    _print_table(results)
    _save_chart(results, list(bars), output_dir / CHART_FILE)


def _output_dir() -> Path:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("output_dir", type=Path, help=f"folder for {CHART_FILE}")
    return parser.parse_args().output_dir


def _finished(bars: pd.DataFrame) -> pd.DataFrame:
    """Drops today's bar: it is still moving."""
    return bars[bars.index < pd.Timestamp.today().normalize()]


def _backtest(run: _Run, bars: pd.DataFrame) -> _Result:
    """One backtest over every finished bar: its summary and its daily equity."""
    entries = consecutive_days(bars[BarColumn.CLOSE], DAYS_IN_A_ROW, run.direction)
    trades, equity = run_backtest(
        bars=bars,
        entries=entries,
        setup=run.setup,
        ticker=run.symbol,
        start=bars.index[0],
        end=bars.index[-1],
        initial_equity=INITIAL_EQUITY,
        trade_size=TRADE_SIZE,
    )
    return _Result(run=run, summary=Summary.from_run(trades, equity, INITIAL_EQUITY), equity=equity)


def _print_table(results: list[_Result]) -> None:
    print(f"{SYSTEM}: {DAYS_IN_A_ROW} days in a row, {HOLDING_DAYS} holding days, "
          f"{TRADE_SIZE:,} per trade on {INITIAL_EQUITY:,}")
    table = pd.DataFrame({
        result.run.label: {name: _format(name, value) for name, value in asdict(result.summary).items()}
        for result in results
    })
    print(table.to_string())


def _format(name: str, value: float | None) -> str:
    """% values and ratios with 2 decimals, money and counts with none; `None` stays `None`."""
    if value is None:
        return str(value)
    if name.endswith(PERCENT_SUFFIX) or name in RATIO_FIELDS:
        return RATIO_FORMAT.format(value)
    return MONEY_FORMAT.format(value)


def _save_chart(results: list[_Result], symbols: list[str], path: Path) -> None:
    figure, panels = plt.subplots(
        2, len(symbols), figsize=FIGURE_SIZE, facecolor=BACKGROUND, sharex="col",
        gridspec_kw={"height_ratios": HEIGHT_RATIOS},
    )
    for column, symbol in enumerate(symbols):
        runs = [result for result in results if result.run.symbol == symbol]
        _draw_ticker(panels[0, column], panels[1, column], symbol, runs)
    figure.tight_layout()
    figure.savefig(path, dpi=DPI, facecolor=BACKGROUND)
    plt.close(figure)


def _draw_ticker(returns: plt.Axes, drawdowns: plt.Axes, symbol: str, runs: list[_Result]) -> None:
    for result in runs:
        color = COLORS[(result.run.direction, result.run.setup)]
        returns.plot(result.equity.index, return_percent(result.equity, INITIAL_EQUITY), color=color,
                     linewidth=LINE_WIDTH, label=_run_label(result))
        drawdowns.plot(result.equity.index, -drawdown_percent(result.equity), color=color,
                       linewidth=LINE_WIDTH)
    dates = runs[0].equity.index
    title = f"{symbol} {dates[0]:%Y}–{dates[-1]:%Y}: {TRADE_SIZE:,} per trade on {INITIAL_EQUITY:,}"
    _style_panel(returns, title, "account return (%)")
    _style_panel(drawdowns, "", "drawdown (%)")
    returns.legend(loc=LEGEND_PLACE, facecolor=BACKGROUND, edgecolor=MUTED, labelcolor=TEXT)


def _run_label(result: _Result) -> str:
    summary = result.summary
    return (f"{result.run.levels}: CAR {summary.annual_return_percent:.2f}%, "
            f"MDD {summary.max_drawdown_percent:.2f}%")


def _style_panel(panel: plt.Axes, title: str, y_label: str) -> None:
    panel.set_facecolor(BACKGROUND)
    panel.set_title(title, color=TEXT)
    panel.set_ylabel(y_label, color=MUTED)
    panel.tick_params(colors=MUTED, which="both")
    for spine in panel.spines.values():
        spine.set_color(MUTED)


if __name__ == "__main__":
    main()
