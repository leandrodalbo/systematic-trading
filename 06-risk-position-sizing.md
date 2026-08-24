# Phase 06 — Risk & Position Sizing

**Goal:** decide how much to risk per trade before a real loss decides it
for you.

## Learn

- Fixed-fractional sizing, volatility-adjusted sizing, the Kelly criterion
  and why full Kelly is too aggressive in practice
- Max drawdown limits and what to do when one is hit
- Correlation risk across positions — crypto and equities can both fall
  together

## Read

- *Systematic Trading* — Robert Carver (position sizing chapters especially) —
  [find it on Amazon](https://www.amazon.co.uk/s?k=Systematic+Trading+Robert+Carver)

## Reading order (~1 hr/day)

1. **Carver, front to back** — it builds from framework to position-sizing
   rules, so early chapters set up terms the sizing chapters rely on. ~344
   pages ÷ ~20 pages/hour ≈ **17 days**. If pressed for time, the
   position-sizing and risk chapters in the second half of the book matter
   most for this phase specifically.

**Phase 06 reading: ~17 days.**

## Steps

1. Add position sizing to your Phase 04 backtest (start with fixed-fractional,
   e.g. risk 1% of equity per trade).
2. Compare fixed-fractional vs. volatility-adjusted sizing on your backtest
   — does one give a smoother equity curve for similar returns?
3. Add a max-drawdown circuit breaker to the backtest (e.g. halt new
   entries if drawdown exceeds X%) and see how it changes the results.
4. Write your risk policy.

## Milestone

- [ ] A written risk policy: max risk per trade, max portfolio drawdown,
      and the rule that halts trading if it's breached.

**Next:** [07-paper-trading-live.md](07-paper-trading-live.md)
