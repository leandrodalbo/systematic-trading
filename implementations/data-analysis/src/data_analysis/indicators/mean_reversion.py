import pandas as pd


def z_score(closes: pd.Series, window: int) -> pd.Series:
    rolling = closes.rolling(window)
    return (closes - rolling.mean()) / rolling.std()
