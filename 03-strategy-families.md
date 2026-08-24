# Phase 03 — Strategy Families

**Goal:** understand mean reversion well enough to explain why it works — and
when it stops working.

## Learn

- Z-score of price vs. moving average, Bollinger Bands, RSI(2)-style
  reversion (Larry Connors)
- Momentum and pairs/cointegration, as contrast — knowing what mean
  reversion *isn't*
- Regime dependence: mean reversion works in ranging markets, fails badly
  in strong trends

## Read

- *Mean Reversion Trading Systems: Practical Methods for Swing Trading* —
  Howard Bandy. Read for the strategy-design logic and validation approach
  only — the code examples are in AmiBroker's AFL, which you won't be
  using. Reimplement every formula yourself in Phase 04. —
  [find it on Amazon](https://www.amazon.com/s?k=Mean+Reversion+Trading+Systems+Howard+Bandy)
- *Algorithmic Trading: Winning Strategies and Their Rationale* — Ernest Chan —
  [find it on Amazon](https://www.amazon.com/s?k=Algorithmic+Trading+Ernest+Chan+Winning+Strategies)

## Steps

1. Read Bandy's book, taking notes on rules rather than copying AFL syntax.
2. Read the mean-reversion chapters of Chan's *Algorithmic Trading*.
3. Pick your specific entry/exit rule set (e.g. z-score of price vs. 20-day
   mean, exit at mean reversion or a stop-loss).
4. Write a one-pager: entry rule, exit rule, and the market condition
   (ranging vs. trending) it depends on.

## Milestone

- [ ] A written one-pager: your strategy's entry rule, exit rule, and the
      market condition it depends on.

**Next:** [04-build-the-system.md](04-build-the-system.md)
