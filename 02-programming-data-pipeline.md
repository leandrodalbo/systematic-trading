# Phase 02 — Programming & Data Pipeline

**Goal:** get a clean, repeatable way to pull price data and hold it
somewhere queryable.

## Learn

- OHLCV data shape, resampling, handling gaps/splits/dividends
- `pandas` if going Python; `ta4j` if staying in Java
- Storing history locally (Parquet / SQLite / Postgres) so you're not
  re-fetching on every run

## Free tools

- `yfinance` — free stock data (Python)
- `ccxt` + Binance API — free crypto data, has a testnet
- `ta4j` — Java-native technical analysis + backtesting library, if you'd
  rather stay in the JVM ecosystem

## Steps

1. Pick a stack: Python (`pandas` + `yfinance`/`ccxt`) or Java (`ta4j`).
2. Write a script that pulls daily OHLCV for one stock and one crypto pair.
3. Store it locally (Parquet or SQLite is enough for now — no need for a
   full Postgres setup yet).
4. Write a small function to resample daily → weekly, to confirm you
   understand the data shape.

## Milestone

- [ ] A script that pulls 2+ years of daily OHLCV for one stock and one
      crypto pair and stores it locally.

**Next:** [03-strategy-families.md](03-strategy-families.md)
