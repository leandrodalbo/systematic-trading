from pathlib import Path

import pandas as pd

from data_analysis.core.config import data_dir
from data_analysis.core.constants import (
    CSV_EXTENSION,
    DATE_INDEX,
    DEFAULT_TIMEFRAME,
    FILE_NAME_SEPARATOR,
    FILE_SYMBOL_SEPARATOR,
    NEW_YORK,
    SYMBOL_SLASH,
    TIME_COLUMN,
)
from data_analysis.core.errors import BarsFileNotFoundError
from data_analysis.domain.asset_class import AssetClass


def load_bars(symbol: str, asset_class: AssetClass, timeframe: str = DEFAULT_TIMEFRAME) -> pd.DataFrame:
    path = _bars_path(symbol, asset_class, timeframe)
    if not path.exists():
        raise BarsFileNotFoundError(path)

    bars = pd.read_csv(path)
    bars.index = _bar_dates(bars.pop(TIME_COLUMN), asset_class)
    return bars.astype(float)


def _bars_path(symbol: str, asset_class: AssetClass, timeframe: str) -> Path:
    file_symbol = symbol.replace(SYMBOL_SLASH, FILE_SYMBOL_SEPARATOR)
    return data_dir() / asset_class.value / f"{file_symbol}{FILE_NAME_SEPARATOR}{timeframe}{CSV_EXTENSION}"


def _bar_dates(times: pd.Series, asset_class: AssetClass) -> pd.DatetimeIndex:
    times = pd.to_datetime(times, utc=True)
    if asset_class is AssetClass.STOCK:
        times = times.dt.tz_convert(NEW_YORK)
    return pd.DatetimeIndex(times.dt.tz_localize(None).dt.normalize(), name=DATE_INDEX)
