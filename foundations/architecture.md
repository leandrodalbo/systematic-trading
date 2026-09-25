# Architecture

## The split

- **Python owns the strategy** — indicators, signals, the official backtest, stats, charts. Every day it answers *"what should I trade today?"*
- **Kotlin + Spring Boot does the plumbing** — fetch and store data, ask Python for today's signals, place orders on Alpaca, scheduling, alerts.

Golden rule: the Python code we backtest is the **same code** that makes live signals. Never rewrite the strategy in Kotlin — two copies drift apart.

## How they talk

- Both read the **same data files**. No price data sent over HTTP.
- Spring asks Python *"signals for today?"*, Python loads the files itself and answers with orders to place (ticker, buy/sell, size).
- Python starts as **scripts/notebooks** (Phases 03–06). It becomes a service (e.g. FastAPI) in Phase 07, when Spring needs daily signals.
- Kotlin never decides a trade. It only carries out what Python says, and checks the risk limits before sending.

## Data layout

```
data/
  stocks/
    AAPL.csv
    SPY.csv
  crypto/
    BTC-USD.csv
    ETH-USD.csv
```

- One CSV per ticker. `/` becomes `-` in file names (`BTC/USD` → `BTC-USD.csv`).
- Columns: `date,open,high,low,close,volume`
- Dates in UTC, oldest first. Stock prices split/dividend adjusted.
- **One source per file.** Never mix Alpaca and Kraken bars in the same file.

## Later

- **Kraken crypto history** — if Alpaca's crypto history is too short (Phase 04 needs 5+ years): download Kraken's OHLCVT CSVs, convert them to our format, and replace the whole file for that ticker. Details in [alpaca-kraken-api-notes.md](alpaca-kraken-api-notes.md).
- **AI news service** — separate service that reads news for a ticker and returns catalysts (earnings, lawsuits, launches…). Spring calls it; results saved as files next to the price data (e.g. `data/news/`).
- **Bigger data** — if CSVs get slow or messy, move to SQLite or Postgres. Same idea, different storage.
