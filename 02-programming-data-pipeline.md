# Phase 02 — Programming & Data Pipeline

**Goal:** get a clean, repeatable way to pull price data and hold it somewhere queryable.

**Status: in progress.** Next: commit 4c, then single writer (cycle 4d), SPECIFY.

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
3. Save each ticker as a CSV in `data/stocks/` or `data/crypto/` (layout in the architecture note).
4. Load the CSVs back and resample daily → weekly, to confirm you understand the data shape.
5. Check how far back Alpaca's crypto history goes. Phase 04 needs 5+ years — if it's short, note it (Kraken CSVs are the fallback).

## Progress (trade-plumber)

Done:
- Alpaca provider fetches one page of bars per call.
- Provider errors become `AlpacaApiFailedException` (with `isRetryable()`).
- `BarRepository` writes and reads CSVs. Default folder: `~/.local/share/trade-plumber` (configurable).
- Writes are atomic (temp file + move) and stream bars from a `Sequence`, so only one page sits in memory.
- `replaceLastAndAppend` keeps every row but the last and appends new bars. Empty input leaves the file alone.
- Both writes reject bars that aren't strictly ascending; the original file stays untouched.
- `MarketDataManager` fetches pages lazily. No stored bars: full history from a start date per asset class (`market-data` props). Stored bars: resume from the last bar. If page 1's first bar has a different time or open (split, dividend, gap), rewrite the whole file.

- **4c** Retries per page (3 retries, 1s doubling). Manager tests use MockK. *Done, not committed yet.*

Next:
- **4d** Single writer. One lock around `update` for all symbols. A second call while one is running fails fast with `UpdateInProgressException` instead of waiting.
- **5a** `POST /market-data/updates` with `symbol`, `assetClass`, `timeframe`. Layers: controller → service → `MarketDataManager`. Returns 204. Controller tests use `springmockk` (`@MockkBean`).
- **5b** `@RestControllerAdvice`: Alpaca failure → 502, update in progress → 409, bad input → 400.
- **5c** Real run (no cycle): paper keys, call the endpoint for one stock and one crypto pair, check the CSVs.
- **6** Python check (step 4, your exercise): first decide how Python finds the data folder (Kotlin writes to `~/.local/share/trade-plumber`, step 3 says `data/` in the repo). Then load one CSV in `implementations/data-analysis` and resample daily → weekly.

Phase 02 is done when 5c and 6 are done. Then tick the milestone.

Rules:
- Incremental updates, not full re-fetches.
- Nothing ever writes data at the same time: one lock in the manager, no overlapping scheduled runs.
- Test doubles: MockK. Controller tests: `springmockk`.

## Milestone

- [ ] A Kotlin job that pulls 2+ years of daily OHLCV for one stock and one crypto pair and stores it as CSV.

**Next:** [03-strategy-families.md](03-strategy-families.md)
