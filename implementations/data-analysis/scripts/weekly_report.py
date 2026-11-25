"""Weekly market report (Saturdays, on Friday's close).

One row of numbers per watch list ticker, saved as CSV in journal/reports/.

Run after ./update_watchlist.sh. The latest candle counts as closed.
"""

from pathlib import Path

import pandas as pd

from data_analysis.core.errors import BarsFileNotFoundError
from data_analysis.data.bars import load_bars
from data_analysis.domain.market import AssetClass, Since
from data_analysis.indicators.mean_reversion import z_score
from data_analysis.indicators.performance import move
from data_analysis.indicators.trend import average_gap, last_cross, vs_average

PROJECT_DIR = Path(__file__).resolve().parents[1]
WATCHLIST = PROJECT_DIR / "watchlist.csv"
REPORTS_DIR = PROJECT_DIR.parents[1] / "journal" / "reports"
CLOSE_COLUMN = "close"
DATE_FORMAT = "%Y-%m-%d"
DECIMALS = "%.2f"

MOVE_DAYS = [1, 7, 30]
RANGE_DAYS = [7, 30]
AVERAGES = [20, 50, 200]
AVERAGE_PAIRS = [(20, 50), (50, 200)]
Z_SCORE_WINDOW = 20
NO_CROSS = ""

UP = "up"
DOWN = "down"


def main() -> None:
    rows, missing = [], []
    for symbol, asset_class in pd.read_csv(WATCHLIST).itertuples(index=False):
        try:
            closes = load_bars(symbol, AssetClass[asset_class])[CLOSE_COLUMN]
        except BarsFileNotFoundError:
            missing.append(symbol)
            continue
        rows.append(_row(symbol, closes))
    path = _save(pd.DataFrame(rows))
    print(f"{len(rows)} tickers saved to {path}")
    if missing:
        print(f"no data: {', '.join(missing)}")


def _row(symbol: str, closes: pd.Series) -> dict:
    row = {"symbol": symbol, "date": f"{closes.index[-1]:{DATE_FORMAT}}", "close": closes.iloc[-1]}
    for days in MOVE_DAYS:
        row[f"move_{days}d"] = _today(move(closes, Since.CLOSE, days))
    for days in RANGE_DAYS:
        row[f"from_high_{days}d"] = _today(move(closes, Since.HIGH, days))
        row[f"from_low_{days}d"] = _today(move(closes, Since.LOW, days))
    for window in AVERAGES:
        row[f"vs_avg{window}"] = _today(vs_average(closes, window))
    for fast, slow in AVERAGE_PAIRS:
        row[f"gap_{fast}_{slow}"] = _today(average_gap(closes, fast, slow))
    for fast, slow in AVERAGE_PAIRS:
        cross = last_cross(closes, fast, slow)
        row[f"cross_{fast}_{slow}"] = cross.kind.value if cross else NO_CROSS
        row[f"cross_{fast}_{slow}_date"] = f"{cross.date:{DATE_FORMAT}}" if cross else NO_CROSS
    row[f"z{Z_SCORE_WINDOW}"] = _today(z_score(closes, Z_SCORE_WINDOW))
    for fast, slow in AVERAGE_PAIRS:
        row[f"trend_{fast}_{slow}"] = UP if row[f"gap_{fast}_{slow}"] > 0 else DOWN
    return row


def _today(values: pd.Series) -> float:
    return values.iloc[-1]


def _save(report: pd.DataFrame) -> Path:
    REPORTS_DIR.mkdir(parents=True, exist_ok=True)
    path = REPORTS_DIR / f"{pd.Timestamp.today():{DATE_FORMAT}}.csv"
    report.to_csv(path, index=False, float_format=DECIMALS)
    return path


if __name__ == "__main__":
    main()
