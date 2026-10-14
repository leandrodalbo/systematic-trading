# Phase 09 — Specialize

**Goal:** decide what "further" means once the first system works. Open-ended: the curriculum ends, ongoing practice begins.

## Learn

- A small portfolio of uncorrelated strategies instead of one system
- Other asset classes or strategy families (momentum, pairs/cointegration)
- ML as a third signal source (next to mean reversion and a second family). Same Phase 05 validation applies: an ML model that "backtests great" is the classic self-taught trap.
- Kraken as a second crypto venue (more coins, deeper book): [foundations/alpaca-kraken-api-notes.md](foundations/alpaca-kraken-api-notes.md)
- If pivoting to quant-dev roles: stochastic calculus and interview probability puzzles

## Read

1. *Advances in Financial Machine Learning* — Marcos López de Prado — [Amazon](https://www.amazon.co.uk/s?k=Advances+in+Financial+Machine+Learning+Lopez+de+Prado). Only once Phases 00–06 are solid, it assumes a lot. ~400 pages, ~15 pages/hr ≈ **27 days**.
2. *Python Machine Learning By Example* (4th ed.) — Yuxi (Hayden) Liu — [Amazon](https://www.amazon.co.uk/s?k=Python+Machine+Learning+By+Example+Yuxi+Liu). General ML, not finance: read for mechanics, not trading advice. Only chapters 1 (skim if scikit-learn/PyTorch is rusty), 5 and 6 (stock prices with regression, then ANNs). ~120 of 518 pages ≈ **8 days**. Read more later if ML becomes a direction.

## Steps

1. Pick a second family (momentum or pairs/cointegration are the natural next step).
2. Build and validate it like Phases 03–05.
3. Check return correlation between the two: the point is that they fail at different times.
4. Run both; compare the combined equity curve to each alone.
5. Optional ML detour: reimplement Liu's ch. 5/6 regression and ANN yourself (don't copy the sample code, same rule as Bandy), then run it through Phase 05 walk-forward/Monte Carlo before trusting it.

## Milestone

- [ ] A second, uncorrelated strategy running alongside the first — the
      actual test of whether you understand the craft or got lucky once.

**See also:** [signals.md](signals.md)
