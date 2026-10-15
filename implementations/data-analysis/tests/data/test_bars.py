import pandas as pd
import pytest

from data_analysis.core.config import DATA_DIR_ENV
from data_analysis.core.errors import BarsFileNotFoundError
from data_analysis.data.bars import load_bars
from data_analysis.domain.asset_class import AssetClass

HEADER = "time,open,high,low,close,volume\n"


@pytest.fixture
def data_folder(monkeypatch, tmp_path):
    monkeypatch.setenv(DATA_DIR_ENV, str(tmp_path))
    return tmp_path


def write_csv(data_folder, folder, file_name, rows):
    path = data_folder / folder / file_name
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(HEADER + "".join(row + "\n" for row in rows))


def test_load_bars_returns_the_ohlcv_columns(data_folder):
    write_csv(data_folder, "stocks", "AAPL_DAY.csv", ["2016-01-04T05:00:00Z,23.07,23.69,22.94,23.69,287741356"])

    bars = load_bars("AAPL", AssetClass.STOCKS)

    assert list(bars.columns) == ["open", "high", "low", "close", "volume"]
    assert bars.iloc[0].to_dict() == {"open": 23.07, "high": 23.69, "low": 22.94, "close": 23.69, "volume": 287741356.0}


def test_load_bars_columns_are_floats(data_folder):
    write_csv(data_folder, "stocks", "AAPL_DAY.csv", ["2016-01-04T05:00:00Z,23,24,22,23,287741356"])

    bars = load_bars("AAPL", AssetClass.STOCKS)

    assert all(dtype == "float64" for dtype in bars.dtypes)


def test_load_bars_index_is_a_plain_date(data_folder):
    write_csv(data_folder, "crypto", "BTC-USD_DAY.csv", ["2021-01-01T00:00:00Z,29255.71,29680.75,28707.56,29418.72,619.419752"])

    bars = load_bars("BTC/USD", AssetClass.CRYPTO)

    assert isinstance(bars.index, pd.DatetimeIndex)
    assert bars.index.name == "date"
    assert bars.index.tz is None


def test_stock_dates_use_the_new_york_date_in_winter_and_summer(data_folder):
    write_csv(data_folder, "stocks", "AAPL_DAY.csv", [
        "2016-01-04T05:00:00Z,1,1,1,1,1",
        "2016-07-01T04:00:00Z,1,1,1,1,1",
    ])

    bars = load_bars("AAPL", AssetClass.STOCKS)

    assert list(bars.index) == [pd.Timestamp("2016-01-04"), pd.Timestamp("2016-07-01")]


def test_crypto_dates_use_the_utc_date(data_folder):
    write_csv(data_folder, "crypto", "BTC-USD_DAY.csv", [
        "2021-01-01T00:00:00Z,1,1,1,1,1",
        "2021-07-01T00:00:00Z,1,1,1,1,1",
    ])

    bars = load_bars("BTC/USD", AssetClass.CRYPTO)

    assert list(bars.index) == [pd.Timestamp("2021-01-01"), pd.Timestamp("2021-07-01")]


def test_load_bars_uses_the_given_timeframe_in_the_file_name(data_folder):
    write_csv(data_folder, "stocks", "AAPL_WEEK.csv", ["2016-01-04T05:00:00Z,1,1,1,1,1"])

    bars = load_bars("AAPL", AssetClass.STOCKS, timeframe="WEEK")

    assert len(bars) == 1


def test_load_bars_fails_with_the_path_when_the_file_is_missing(data_folder):
    expected_path = data_folder / "crypto" / "ETH-USD_DAY.csv"

    with pytest.raises(BarsFileNotFoundError, match=str(expected_path)):
        load_bars("ETH/USD", AssetClass.CRYPTO)
