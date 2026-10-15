from pathlib import Path

import pandas as pd

from data_analysis.core.config import data_dir
from data_analysis.core.constants import (
    DATE_INDEX,
    DEFAULT_TIMEFRAME,
    FILE_SYMBOL_SEPARATOR,
    NEW_YORK,
    SYMBOL_SLASH,
    TIME_COLUMN,
)
from data_analysis.core.errors import BarsFileNotFoundError
from data_analysis.domain.asset_class import AssetClass


def load_bars(symbol: str, asset_class: AssetClass, timeframe: str = DEFAULT_TIMEFRAME) -> pd.DataFrame:
    path = bars_path(symbol, asset_class, timeframe)
    if not path.exists():
        raise BarsFileNotFoundError(path)

    bars = pd.read_csv(path)
    times = pd.to_datetime(bars.pop(TIME_COLUMN), utc=True)
    if asset_class is AssetClass.STOCKS:
        times = times.dt.tz_convert(NEW_YORK)
    bars.index = pd.DatetimeIndex(times.dt.tz_localize(None).dt.normalize(), name=DATE_INDEX)
    return bars.astype(float)


def bars_path(symbol: str, asset_class: AssetClass, timeframe: str) -> Path:
    file_symbol = symbol.replace(SYMBOL_SLASH, FILE_SYMBOL_SEPARATOR)
    return data_dir() / asset_class.value / f"{file_symbol}_{timeframe}.csv"
