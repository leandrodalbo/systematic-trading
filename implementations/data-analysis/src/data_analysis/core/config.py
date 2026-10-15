import os
from pathlib import Path

from data_analysis.core.errors import DataDirNotSetError

DATA_DIR_ENV = "TRADE_PLUMBER_DATA_DIR"


def data_dir() -> Path:
    value = os.environ.get(DATA_DIR_ENV)
    if not value:
        raise DataDirNotSetError(DATA_DIR_ENV)
    return Path(value)
