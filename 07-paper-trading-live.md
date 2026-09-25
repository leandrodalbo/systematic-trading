# Phase 07 — Paper Trading → Small Live Capital

**Goal:** run the system for real, starting with money you can't lose in a
way that matters.

**Note on timing:** this phase has a hard minimum of about 3 months
wall-clock time. It can't be compressed by working more hours — the market
needs to generate enough sample trades to mean anything.

## Learn

- The gap between backtest and paper results, and between paper and live
  results
- Trade journaling — record the rationale, not just the fill
- Slippage and fee reality-checking against your backtest assumptions

## Free tools

- Alpaca paper trading (stocks and crypto, free). It doesn't simulate
  slippage, market impact or fees — expect live to be a bit worse than
  paper. Details:
  [foundations/alpaca-kraken-api-notes.md](foundations/alpaca-kraken-api-notes.md)

## Steps

1. Turn the Phase 04 Python signal code into a small service (e.g.
   FastAPI) that answers "what should I trade today?" — the same code the
   backtest used, not a copy.
2. In the Spring Boot app: call that service each day, check the risk
   limits from Phase 06, and send the orders to Alpaca's paper trading API.
3. Run it live (paper) for at least 3 months. Don't touch the parameters
   mid-run — that defeats the point.
4. Journal every trade: entry reason, exit reason, what actually happened.
5. At the end of the period, compare paper results against backtest
   expectations (win rate, average win/loss, drawdown). Investigate any
   large gap.

## Milestone

- [ ] Three months of paper-traded results that track your backtest's
      expectations within a reasonable margin.

**Next:** [08-infrastructure.md](08-infrastructure.md) (can run in parallel
with this phase)
