# Phase 04 — Build the System (the final project)

**Goal:** turn the Phase 03 one-pager into code: a signal generator plus a backtest engine that scores it. This is the "Mean Reversion Trading System" deliverable.

## Learn

- Vectorized vs. event-driven backtesting, and why event-driven avoids look-ahead bias
- Position sizing basics; honest fees + slippage (from Phase 01)
- Reporting: equity curve, drawdown curve, win rate, average win/loss, Sharpe

## Free tools

- Python (`pandas`): indicators, signals, a hand-rolled bar-by-bar backtest loop. Hand-rolling teaches more than a framework the first time. Write the signal code so the backtest and the live service (Phase 07) call the same functions ([foundations/architecture.md](foundations/architecture.md)).
- `matplotlib` for report charts.
- `vectorbt` (optional, later): fast parameter sweeps once the hand-rolled version works.
- QuantConnect / LEAN: see below.

## QuantConnect / LEAN (note for later)

Cloud backtesting + live trading platform on LEAN, their open-source (Apache 2.0) event-driven engine. Python or C#.

- **Free cloud tier:** browser backtests on slow shared nodes (one at a time), Jupyter notebooks (`QuantBook`), their data inside the cloud (US equities/ETFs, options, futures, forex, crypto; survivorship-bias-free, corporate actions handled).
- **Not free:** downloading their data, live trading (usually cloud paper trading too), faster/parallel nodes, large optimizations, premium alt-data.
- **Our own data → run LEAN locally (free):** clone LEAN or use the `lean` CLI + Docker. Ships with sample data only (SPY and a few others). Feed it our Phase 02 CSVs (convert to LEAN format or load as custom data). Then data quality is on us: splits, dividends, gaps, survivorship. Local live trading via supported brokers also works.

**Use here:** a cross-check, not our engine. Prototype in the free cloud on their clean data, compare with our Python backtest. If they disagree, it's usually a bug in our data pipeline. Tiers change: check quantconnect.com/pricing first.

## Steps

1. Implement the signal generator from the Phase 03 rules on the Phase 02 data.
2. Build a backtest engine: bar-by-bar, fees/slippage applied, every trade logged.
3. Report: equity curve, drawdown curve, win rate, average win/loss, Sharpe.
4. Run on one stock and one crypto pair, 5+ years. If Alpaca's crypto history is too short, bring in Kraken's CSVs first (see the architecture note).
5. Sanity-check the equity curve against what you know about the instrument. Suspicious spikes usually mean a bug (look-ahead, data gap, fee error).

## Milestone

- [ ] A backtest report for your mean-reversion system across one stock and
      one crypto pair, 5+ years of data.

**Next:** [05-statistical-validation.md](05-statistical-validation.md)
