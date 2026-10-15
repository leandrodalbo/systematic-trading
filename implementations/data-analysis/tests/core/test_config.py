import pytest

from data_analysis.core.config import DATA_DIR_ENV, data_dir
from data_analysis.core.errors import DataDirNotSetError


def test_data_dir_returns_the_folder_from_the_env_var(monkeypatch, tmp_path):
    monkeypatch.setenv(DATA_DIR_ENV, str(tmp_path))

    assert data_dir() == tmp_path


def test_data_dir_fails_when_the_env_var_is_missing(monkeypatch):
    monkeypatch.delenv(DATA_DIR_ENV, raising=False)

    with pytest.raises(DataDirNotSetError, match="TRADE_PLUMBER_DATA_DIR"):
        data_dir()


def test_data_dir_fails_when_the_env_var_is_empty(monkeypatch):
    monkeypatch.setenv(DATA_DIR_ENV, "")

    with pytest.raises(DataDirNotSetError, match="TRADE_PLUMBER_DATA_DIR"):
        data_dir()
