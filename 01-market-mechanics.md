# Phase 01 — Market Mechanics

**Status: completed 2026-09-25.**

**Goal:** understand what actually happens when an order is placed, on both
stock and crypto venues.

## Learn

- Order types (market/limit/stop), bid-ask spread, slippage
- Where stocks differ from crypto: trading hours, settlement, custody, fees,
  24/7 liquidity gaps
- Why backtests that ignore fees and slippage lie to you

## Read

- Chapters 1–3 of *Quantitative Trading* — Ernest Chan — **finished** —
  [find it on Amazon](https://www.amazon.co.uk/s?k=Quantitative+Trading+Ernest+Chan)

## Reading order (~1 hr/day)

1. **_Quantitative Trading_, chapters 1–3 only** (you already have this book
   from Phase 00). ~80 pages ÷ ~20 pages/hour ≈ **4 days**.

**Phase 01 reading: ~4 days.**

## Free tools

- [Alpaca API docs](https://docs.alpaca.markets/) (stocks and crypto) —
  read the docs even before writing code, they teach the mechanics
- Summary: [foundations/alpaca-kraken-api-notes.md](foundations/alpaca-kraken-api-notes.md)
- Kraken (crypto) is parked until the Alpaca setup is solid — its notes
  are in the same file.

## Steps

1. Read Chan chapters 1–3.
2. Read through the Alpaca API docs (stocks and crypto) — order types, fee
   schedules, rate limits.
3. Write a short comparison list: stocks vs. crypto, on hours, settlement,
   fees, and liquidity. —
   [foundations/stocks-vs-crypto-notes.md](foundations/stocks-vs-crypto-notes.md)

## Milestone

- [x] You can list three ways a live strategy underperforms its backtest, and why.
  Answer: [foundations/backtesting-notes.md](foundations/backtesting-notes.md) (last section).

**Next:** [02-programming-data-pipeline.md](02-programming-data-pipeline.md)

