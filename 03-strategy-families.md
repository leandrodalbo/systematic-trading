# Phase 03 — Strategy Families

**Goal:** understand mean reversion well enough to explain why it works, and when it stops working.

## Learn

- Z-score of price vs. moving average, Bollinger Bands, RSI(2) reversion (Larry Connors)
- Momentum and pairs/cointegration, as contrast: what mean reversion *isn't*
- Regimes: mean reversion works in ranging markets, fails badly in strong trends

## Read (~28 days at 1 hr/day, the densest block: take notes as you go)

1. *Mean Reversion Trading Systems* — Howard Bandy — [Amazon](https://www.amazon.co.uk/s?k=Mean+Reversion+Trading+Systems+Howard+Bandy). Read first, it's the spec for the final project. ~342 pages ≈ **17 days**, front to back; the later chapters with named example systems are the most reusable. Read for design and validation logic only: its code is AmiBroker AFL, so reimplement every formula yourself in Phase 04.
2. *Algorithmic Trading: Winning Strategies and Their Rationale* — Ernest Chan — [Amazon](https://www.amazon.co.uk/s?k=Algorithmic+Trading+Ernest+Chan+Winning+Strategies). For the momentum/pairs contrast. ~224 pages ≈ **11 days**.

## Free tools

- Python notebook (`pandas`) on the Phase 02 CSVs: plot a z-score or RSI(2) on your own data. This becomes the start of the Phase 04 strategy code.

## Steps

1. Read Bandy, noting rules, not AFL syntax.
2. Read the mean-reversion chapters of Chan.
3. Pick your entry/exit rules (e.g. z-score of price vs. 20-day mean; exit at the mean or a stop-loss).
4. Write the one-pager.

## Milestone

- [ ] A written one-pager: your strategy's entry rule, exit rule, and the
      market condition (ranging vs. trending) it depends on.

**Next:** [04-build-the-system.md](04-build-the-system.md)
