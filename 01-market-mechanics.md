# Phase 01 — Market Mechanics

**Status: completed 2026-09-25.**

**Goal:** understand what happens when an order is placed, on stock and crypto venues.

## Learn

- Order types (market/limit/stop), bid-ask spread, slippage
- Stocks vs. crypto: trading hours, settlement, custody, fees, 24/7 liquidity gaps
- Why backtests that ignore fees and slippage lie

## Read

- *Quantitative Trading* — Ernest Chan, chapters 1–3 — **finished** — [Amazon](https://www.amazon.co.uk/s?k=Quantitative+Trading+Ernest+Chan). ~80 pages, ~20 pages/hr ≈ **4 days**.

## Free tools

- [Alpaca API docs](https://docs.alpaca.markets/) (stocks and crypto). Read them before coding, they teach the mechanics. Summary: [foundations/alpaca-kraken-api-notes.md](foundations/alpaca-kraken-api-notes.md).
- Kraken (crypto) is parked until Alpaca is solid. Its notes are in the same file.

## Steps

1. Read Chan chapters 1–3.
2. Read the Alpaca docs: order types, fees, rate limits.
3. Compare stocks vs. crypto on hours, settlement, fees, liquidity → [foundations/stocks-vs-crypto-notes.md](foundations/stocks-vs-crypto-notes.md)

## Milestone

- [x] You can list three ways a live strategy underperforms its backtest, and why.
  Answer: [foundations/backtesting-notes.md](foundations/backtesting-notes.md) (last section).

**Next:** [02-programming-data-pipeline.md](02-programming-data-pipeline.md)
