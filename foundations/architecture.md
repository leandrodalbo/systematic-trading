# Architecture

## The split

- **Python owns the strategy:** indicators, signals, the official backtest, stats, charts. Each day it answers *"what should I trade today?"*
- **Kotlin + Spring Boot does the plumbing:** fetch and store data, ask Python for signals, place orders on Alpaca, scheduling, alerts.

Golden rule: the Python code we backtest is the **same code** that makes live signals. Never rewrite the strategy in Kotlin; two copies drift apart.

## How they talk

- Both read the **same data files**. No price data over HTTP.
- Spring asks Python *"signals for today?"*. Python loads the files itself and answers with orders (ticker, buy/sell, size).
- Python starts as **scripts/notebooks** (Phases 03–06) and becomes a service (e.g. FastAPI) in Phase 07.
- Kotlin never decides a trade. It carries out what Python says and checks risk limits before sending.

## Data layout (as trade-plumber writes it)

```
~/.local/share/trade-plumber/      # storage.baseDir ($XDG_DATA_HOME/trade-plumber)
  stocks/
    AAPL_DAY.csv
  crypto/
    BTC-USD_DAY.csv
```

- One CSV per ticker and timeframe: `<SYMBOL>_<TIMEFRAME>.csv`. `/` becomes `-` (`BTC/USD` → `BTC-USD`). Timeframes: `HOURS1, HOURS4, DAY, WEEK, MONTH`.
- Header: `time,open,high,low,close,volume`. Time is a UTC ISO instant, oldest first. Stock prices split/dividend adjusted.
- **One source per file.** Never mix Alpaca and Kraken bars.
- Open question (Phase 02 step 6): how Python finds this folder.

## Later

- **Kraken crypto history:** if Alpaca's is too short (Phase 04 needs 5+ years), download Kraken's OHLCVT CSVs, convert to our format, replace the whole file for that ticker. See [alpaca-kraken-api-notes.md](alpaca-kraken-api-notes.md).
- **Bigger data:** if CSVs get slow or messy, move to SQLite or Postgres.
