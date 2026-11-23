"""Are daily returns a bell curve? Four moments + histogram vs a normal curve, SPY and BTC."""

import argparse
from pathlib import Path
from statistics import NormalDist

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

from data_analysis.data.bars import load_bars
from data_analysis.domain.asset_class import AssetClass

TICKERS = [
    ("SPY", AssetClass.STOCK),
    ("BTC/USD", AssetClass.CRYPTO),
]
CLOSE_COLUMN = "close"
PERCENT = 100

MEAN = "mean"
STD = "std"
SKEW = "skew"
KURT = "kurt"
MOMENTS = [MEAN, STD, SKEW, KURT]
TAIL_STD_DEVS = 3

CHART_FILE = "chart.png"
BINS = 100
CURVE_POINTS = 500
LOG_FLOOR_SHARE = 0.5
FIGURE_SIZE = (12, 4.5)
DPI = 150
LEGEND_PLACE = "upper left"
BACKGROUND = "#0b0f14"
TEXT = "#e6edf3"
MUTED = "#8b949e"
BARS = "#22c55e"
CURVE = "#f87171"


def main() -> None:
    output_dir = _output_dir()
    returns = {symbol: _daily_returns(symbol, asset_class) for symbol, asset_class in TICKERS}
    moments = {symbol: daily.agg(MOMENTS) for symbol, daily in returns.items()}
    for symbol, daily in returns.items():
        _print_summary(symbol, daily, moments[symbol])
    _save_chart(returns, moments, output_dir / CHART_FILE)


def _output_dir() -> Path:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("output_dir", type=Path, help=f"folder for {CHART_FILE} (the Note's folder)")
    return parser.parse_args().output_dir


def _daily_returns(symbol: str, asset_class: AssetClass) -> pd.Series:
    closes = load_bars(symbol, asset_class)[CLOSE_COLUMN]
    finished = closes[closes.index < pd.Timestamp.today().normalize()]
    return finished.pct_change().dropna() * PERCENT


def _print_summary(symbol: str, daily: pd.Series, moments: pd.Series) -> None:
    print(f"{symbol}: {len(daily)} days, {daily.index[0]:%Y-%m-%d} to {daily.index[-1]:%Y-%m-%d}")
    print(f"  mean             {moments[MEAN]:+.3f}%")
    print(f"  std dev          {moments[STD]:.3f}%")
    print(f"  skewness         {moments[SKEW]:+.2f}")
    print(f"  excess kurtosis  {moments[KURT]:+.2f}  (normal = 0)")
    print(f"  days beyond {TAIL_STD_DEVS} std devs: {_tail_days(daily, moments)}, "
          f"a bell curve expects {_expected_tail_days(len(daily)):.0f}")
    print()


def _tail_days(daily: pd.Series, moments: pd.Series) -> int:
    distance = (daily - moments[MEAN]).abs()
    return int((distance > TAIL_STD_DEVS * moments[STD]).sum())


def _expected_tail_days(days: int) -> float:
    both_tails = 2 * NormalDist().cdf(-TAIL_STD_DEVS)
    return days * both_tails


def _save_chart(returns: dict[str, pd.Series], moments: dict[str, pd.Series], path: Path) -> None:
    figure, panels = plt.subplots(1, len(returns), figsize=FIGURE_SIZE, facecolor=BACKGROUND)
    for panel, (symbol, daily) in zip(panels, returns.items()):
        _draw_panel(panel, symbol, daily, moments[symbol])
    figure.tight_layout()
    figure.savefig(path, dpi=DPI, facecolor=BACKGROUND)
    plt.close(figure)


def _draw_panel(panel: plt.Axes, symbol: str, daily: pd.Series, moments: pd.Series) -> None:
    densities, _, _ = panel.hist(daily, bins=BINS, density=True, color=BARS, label="daily returns")
    _draw_bell_curve(panel, daily, moments)
    panel.set_yscale("log")
    panel.set_ylim(bottom=densities[densities > 0].min() * LOG_FLOOR_SHARE)
    title = (f"{symbol} {daily.index[0]:%Y}–{daily.index[-1]:%Y}: "
             f"skew {moments[SKEW]:+.2f}, excess kurtosis {moments[KURT]:+.2f}")
    _style_panel(panel, title)


def _draw_bell_curve(panel: plt.Axes, daily: pd.Series, moments: pd.Series) -> None:
    bell = NormalDist(moments[MEAN], moments[STD])
    x = np.linspace(daily.min(), daily.max(), CURVE_POINTS)
    panel.plot(x, [bell.pdf(value) for value in x], color=CURVE, linewidth=2,
               label="bell curve, same mean and std dev")


def _style_panel(panel: plt.Axes, title: str) -> None:
    panel.set_facecolor(BACKGROUND)
    panel.set_title(title, color=TEXT)
    panel.set_xlabel("daily return (%)", color=MUTED)
    panel.set_ylabel("share of days (log scale)", color=MUTED)
    panel.tick_params(colors=MUTED, which="both")
    for spine in panel.spines.values():
        spine.set_color(MUTED)
    panel.legend(loc=LEGEND_PLACE, facecolor=BACKGROUND, edgecolor=MUTED, labelcolor=TEXT)


if __name__ == "__main__":
    main()
