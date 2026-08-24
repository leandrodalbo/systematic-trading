# Phase 04 — Build the System (the final project)

**Goal:** turn the Phase 03 one-pager into running code: a signal generator
plus a backtest engine that scores it. This is the actual "Mean Reversion
Trading System" deliverable.

## Learn

- Vectorized vs. event-driven backtesting, and why event-driven avoids
  look-ahead bias
- Position sizing basics, transaction costs modeled honestly (fees +
  slippage, from Phase 01's research)
- Reporting: equity curve, drawdown curve, win rate, average win/loss,
  Sharpe ratio

## Free tools

- `backtrader` or `vectorbt` (Python)
- Hand-rolled loop over `ta4j` (Java) — for a first system, hand-rolling
  teaches more than a framework does

## Steps

1. Implement the signal generator from your Phase 03 rules against the
   stored data from Phase 02.
2. Build (or wire up) a backtest engine that walks through the data
   bar-by-bar, applies fees/slippage, and logs every trade.
3. Generate a report: equity curve, drawdown curve, win rate, average
   win/loss, Sharpe ratio.
4. Run it against one stock and one crypto pair, 5+ years of data.
5. Sanity-check: does the equity curve make sense given what you know
   about that instrument's history? Any suspicious spikes usually mean a
   bug (look-ahead bias, a data gap, a fee miscalculation).

## Milestone

- [ ] A backtest report for your mean-reversion system across one stock and
      one crypto pair, 5+ years of data.

**Next:** [05-statistical-validation.md](05-statistical-validation.md)
