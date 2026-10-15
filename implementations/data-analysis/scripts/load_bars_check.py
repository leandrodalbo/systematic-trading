from data_analysis.data.bars import load_bars
from data_analysis.domain.asset_class import AssetClass

TICKERS = [
    ("AAPL", AssetClass.STOCK),
    ("BTC/USD", AssetClass.CRYPTO),
]
FIRST_AND_LAST_ROW = [0, -1]


def main() -> None:
    for symbol, asset_class in TICKERS:
        bars = load_bars(symbol, asset_class)
        print(f"{symbol}: {len(bars)} bars")
        print(bars.iloc[FIRST_AND_LAST_ROW])
        print()


if __name__ == "__main__":
    main()
