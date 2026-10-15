# Phase 02 — Programming & Data Pipeline

**Goal:** get a clean, repeatable way to pull price data and hold it somewhere queryable.

**Status: completed 2026-10-15.**

Stack and data layout: [foundations/architecture.md](foundations/architecture.md).

## Learn

- OHLCV data shape, resampling, handling gaps/splits/dividends
- Stocks trade on weekdays only, crypto every day — the two series won't line up day by day
- Calling a REST API from Kotlin + Spring Boot: auth headers, pagination, rate limits
- Storing history locally (one CSV per ticker) so you're not re-fetching on every run

## Free tools

- Alpaca Market Data API — free stock and crypto bars with the same keys as your paper account. Endpoints and gotchas: [foundations/alpaca-kraken-api-notes.md](foundations/alpaca-kraken-api-notes.md)
- Kotlin + Spring Boot (`RestClient`) — no SDK needed, it's plain REST

## Steps

1. Create Alpaca paper API keys. Keep them in environment variables or a git-ignored `.env` — never in code.
2. Build a Kotlin/Spring job that pulls daily bars for one stock (stocks: `feed=sip`, `adjustment=all`) and one crypto pair.
3. Save each ticker as a CSV under `~/.local/share/trade-plumber/stocks/` or `crypto/` (layout in the architecture note).
4. Load the CSVs back in Python, to confirm you understand the data shape.
5. Check how far back Alpaca's crypto history goes. Phase 04 needs 5+ years — if it's short, note it (Kraken CSVs are the fallback).

## Progress (trade-plumber)

Done:
- Alpaca provider fetches one page of bars per call.
- Provider errors become `AlpacaApiFailedException` (with `isRetryable()`).
- `BarRepository` writes and reads CSVs in the folder set by `TRADE_PLUMBER_DATA_DIR` (no default).
- Writes are atomic (temp file + move) and stream bars from a `Sequence`, so only one page sits in memory.
- `replaceLastAndAppend` keeps every row but the last and appends new bars. Empty input leaves the file alone.
- Both writes reject bars that aren't strictly ascending; the original file stays untouched.
- `MarketDataManager` fetches pages lazily. No stored bars: full history from a start date per asset class (`market-data` props). Stored bars: resume from the last bar. If page 1's first bar has a different time or open (split, dividend, gap), rewrite the whole file.
- Each page is retried on retryable Alpaca errors (3 retries, 1s doubling). Manager tests use MockK.
- Single writer: one `Semaphore` around `update` for all symbols. A second call while one is running fails fast with `UpdateInProgressException` instead of waiting.
- `POST /market-data/updates` with a JSON body (`symbol`, `assetClass`, `timeframe`) returns 204. Layers: controller → service → `MarketDataManager`. Controller tests use `springmockk` (`@MockkBean`).
- Errors (`ApiExceptionHandler`, `@RestControllerAdvice`, `ProblemDetail` body): Alpaca failure → 502, update in progress → 409, unreadable body → 400, anything else → 500 "Unexpected error" (real message never leaks).

- **5c real run (2026-10-14):** `AAPL_DAY.csv` 2,711 bars (2016-01-04 → today, split-adjusted: 2016 open $23.07) and `BTC-USD_DAY.csv` 2,113 bars (2021-01-01 → today). Run steps: `implementations/testing/using-trade-plumber.md`.
  - Found a bug: the free plan blocks SIP data from the last 15 min, and stock requests ended at *now* → 403 → 502. Fix: stock requests end 16 min early (`SIP_DELAY_MINUTES`); crypto unchanged.
  - Step 5 answered: crypto history covers our 2021-01-01 start (~5.8 years), enough for Phase 04. No Kraken needed yet.

- **6 Python check (2026-10-15):** `implementations/data-analysis` set up (uv, pytest, `src/` layout: `core/`, `domain/`, `data/`). `load_bars(symbol, asset_class)` reads a CSV into a DataFrame indexed by plain date (stocks: New York date, crypto: UTC date). `scripts/load_bars_check.py` loads AAPL and BTC/USD from the real files.
  - One data folder variable for both sides: `TRADE_PLUMBER_DATA_DIR` (set in `~/.zshrc`). Kotlin's `storage.baseDir` reads only it.

Later (small, optional cycles): fail at startup when `TRADE_PLUMBER_DATA_DIR` is missing (today Spring keeps the literal `${TRADE_PLUMBER_DATA_DIR}` as the folder name and would write there); log unexpected errors in the 500 handler (right now they leave no trace, which made the 5c bug hard to see); reject a blank `symbol` with 400 (needs `spring-boot-starter-validation`); move the 204 controller test onto the `postUpdate` helper.

Rules:
- Incremental updates, not full re-fetches.
- Nothing ever writes data at the same time: one lock in the manager, no overlapping scheduled runs.
- Test doubles: MockK. Controller tests: `springmockk`.

## Milestone

- [x] A Kotlin job that pulls 2+ years of daily OHLCV for one stock and one crypto pair and stores it as CSV.

**Next:** [03-strategy-families.md](03-strategy-families.md)
