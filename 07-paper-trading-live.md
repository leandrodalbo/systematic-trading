# Phase 07 — Paper Trading → Small Live Capital

**Goal:** run the system for real, starting with money that can't hurt.

**Timing:** a hard minimum of ~3 months wall-clock. More hours don't speed it up; the market has to produce enough trades.

## Learn

- The gap between backtest → paper → live results
- Trade journaling: record the reason, not just the fill
- Checking real slippage and fees against backtest assumptions

## Free tools

- Alpaca paper trading (stocks and crypto, free). No slippage, market impact or fees simulated, so live will be a bit worse. Details: [foundations/alpaca-kraken-api-notes.md](foundations/alpaca-kraken-api-notes.md)

## Steps

1. Turn the Phase 04 Python signal code into a small service (e.g. FastAPI) answering "what should I trade today?". Same code the backtest used, not a copy.
2. Spring Boot app: call it daily, check the Phase 06 risk limits, send orders to Alpaca paper.
3. Run for at least 3 months. Don't touch parameters mid-run.
4. Journal every trade: entry reason, exit reason, what happened.
5. At the end, compare with backtest expectations (win rate, avg win/loss, drawdown). Investigate any big gap.

## Milestone

- [ ] Three months of paper-traded results that track your backtest's
      expectations within a reasonable margin.

**Next:** [08-infrastructure.md](08-infrastructure.md) (can run in parallel)
