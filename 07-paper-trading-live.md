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

- Alpaca paper trading (stocks, free)
- Binance testnet (crypto, free)

## Steps

1. Wire your Phase 04 signal logic up to Alpaca's and/or Binance's paper/
   testnet APIs.
2. Run it live (paper) for at least 3 months. Don't touch the parameters
   mid-run — that defeats the point.
3. Journal every trade: entry reason, exit reason, what actually happened.
4. At the end of the period, compare paper results against backtest
   expectations (win rate, average win/loss, drawdown). Investigate any
   large gap.

## Milestone

- [ ] Three months of paper-traded results that track your backtest's
      expectations within a reasonable margin.

**Next:** [08-infrastructure.md](08-infrastructure.md) (can run in parallel
with this phase)
