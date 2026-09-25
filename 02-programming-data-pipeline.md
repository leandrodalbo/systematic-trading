# Phase 02 — Programming & Data Pipeline

**Goal:** get a clean, repeatable way to pull price data and hold it somewhere queryable.

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

## Milestone

- [ ] A Kotlin job that pulls 2+ years of daily OHLCV for one stock and one crypto pair and stores it as CSV.

**Next:** [03-strategy-families.md](03-strategy-families.md)
