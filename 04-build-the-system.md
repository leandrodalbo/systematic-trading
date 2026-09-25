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

- Python (`pandas`) — indicators, signals and a hand-rolled bar-by-bar
  backtest loop. For a first system, hand-rolling teaches more than a
  framework does. Write the signal code so the backtest and the live
  service (Phase 07) call the same functions
  ([foundations/architecture.md](foundations/architecture.md))
- `matplotlib` for the report charts
- `vectorbt` (optional, later) — fast parameter sweeps once the hand-rolled
  version works
- QuantConnect / LEAN — see below

## QuantConnect / LEAN (note for later)

Cloud platform for backtesting + live trading, built on LEAN, their
open-source (Apache 2.0) event-driven engine. Python or C#.

**Free (cloud tier):**

- Browser backtesting on shared, slow nodes (one at a time)
- Jupyter research notebooks (`QuantBook`)
- Their historical data *inside the cloud*: US equities/ETFs, options,
  futures, forex, crypto — survivorship-bias-free, corporate actions handled

**Not free:** downloading their data (cloud-only license), live trading
(and usually cloud paper trading), faster/parallel nodes, optimization at
scale, premium alt-data.

**Backtesting with our own data → run LEAN locally (free):**

- Clone LEAN (or use the `lean` CLI + Docker) and backtest on your machine
- Ships with sample data only (SPY and a few others) — we supply the rest
- Feed it our Phase 02 CSVs by converting to LEAN's data format or loading
  it as custom data
- We own data quality then: splits, dividends, gaps, survivorship bias
- Local live trading through supported brokers is also possible

**How to use it here:** a cross-check, not our engine. Prototype an idea in
the free cloud tier on their clean data, then compare with our Python
backtest. If the results disagree, it's usually a bug in our data pipeline.

Pricing/tiers change — check quantconnect.com/pricing before relying on it.

## Steps

1. Implement the signal generator from your Phase 03 rules against the
   stored data from Phase 02.
2. Build (or wire up) a backtest engine that walks through the data
   bar-by-bar, applies fees/slippage, and logs every trade.
3. Generate a report: equity curve, drawdown curve, win rate, average
   win/loss, Sharpe ratio.
4. Run it against one stock and one crypto pair, 5+ years of data. If
   Alpaca's crypto history is too short, bring in Kraken's CSVs first (see
   the architecture note).
5. Sanity-check: does the equity curve make sense given what you know
   about that instrument's history? Any suspicious spikes usually mean a
   bug (look-ahead bias, a data gap, a fee miscalculation).

## Milestone

- [ ] A backtest report for your mean-reversion system across one stock and
      one crypto pair, 5+ years of data.

**Next:** [05-statistical-validation.md](05-statistical-validation.md)
