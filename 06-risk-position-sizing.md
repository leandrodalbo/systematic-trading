# Phase 06 — Risk & Position Sizing

**Goal:** decide how much to risk per trade before a real loss decides it for you.

## Learn

- Fixed-fractional and volatility-adjusted sizing; Kelly, and why full Kelly is too aggressive
- Max drawdown limits and what to do when one is hit
- Correlation risk: crypto and equities can fall together

## Read

- *Systematic Trading* — Robert Carver — [Amazon](https://www.amazon.co.uk/s?k=Systematic+Trading+Robert+Carver). ~344 pages ≈ **17 days**, front to back (early chapters set up terms the sizing chapters use). If pressed, the sizing and risk chapters in the second half matter most.

## Steps

1. Add sizing to the Phase 04 backtest (start fixed-fractional, e.g. 1% of equity per trade).
2. Compare fixed-fractional vs. volatility-adjusted: smoother equity curve for similar returns?
3. Add a max-drawdown circuit breaker (halt new entries past X% drawdown) and see the effect.
4. Write your risk policy.

## Milestone

- [ ] A written risk policy: max risk per trade, max portfolio drawdown,
      and the rule that halts trading if it's breached.

**Next:** [07-paper-trading-live.md](07-paper-trading-live.md)
