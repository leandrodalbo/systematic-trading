# Phase 09 — Specialize

**Goal:** decide what "further" means once the first system works. Open-ended
— this is where the curriculum ends and ongoing practice begins.

## Learn

- Running a small portfolio of uncorrelated strategies rather than one
  system
- Extending into other asset classes, or other strategy families (momentum,
  pairs/cointegration)
- Using ML as a third signal source (alongside the rule-based mean-reversion
  system and a second strategy family) — same validation discipline from
  Phase 05 applies, an ML model that "backtests great" is the classic way
  self-taught traders fool themselves
- If pivoting toward a quant-dev role: brush up stochastic calculus and
  interview-style probability puzzles

## Read

- *Advances in Financial Machine Learning* — Marcos López de Prado (once
  the fundamentals from Phases 00–06 are solid, not before — it assumes a
  lot) —
  [find it on Amazon](https://www.amazon.co.uk/s?k=Advances+in+Financial+Machine+Learning+Lopez+de+Prado)
- *Python Machine Learning By Example* (4th ed.) — Yuxi (Hayden) Liu. General
  ML text, not finance-specific — read it for the ML mechanics, not trading
  advice. Chapters 5 and 6 (predicting stock prices with regression, then
  ANNs) are the directly relevant ones; skim Chapter 1 first if scikit-learn/
  PyTorch basics are rusty. —
  [find it on Amazon](https://www.amazon.co.uk/s?k=Python+Machine+Learning+By+Example+Yuxi+Liu)

## Reading order (~1 hr/day)

1. **López de Prado, front to back**, but only after Phases 00–06 are
   solid — it assumes you already have the statistics and backtesting
   grounding. ~400 pages ÷ ~15 pages/hour (dense, code-heavy) ≈ **27 days**.
2. **Liu, Chapters 1, 5, and 6 only** (~120 pages of the 518-page 4th
   edition) ÷ ~15 pages/hour (code-heavy) ≈ **8 days**. Come back for the
   rest of the book later if ML turns out to be a direction worth going
   deeper on.

**Phase 09 reading: ~27 days (López de Prado) + ~8 days (Liu, targeted
chapters).**

## Steps

1. Pick a second strategy family (momentum or pairs/cointegration are the
   natural next step after mean reversion).
2. Build and validate it the same way as Phase 03–05.
3. Check correlation between the two strategies' returns — the point of a
   second strategy is that it fails at different times than the first.
4. Run both in parallel and compare the combined equity curve to either one
   alone.
5. Optional ML detour: reimplement Liu's Chapter 5/6 stock-price regression
   and ANN examples yourself (don't copy the sample code verbatim — same
   rule as Bandy in Phase 03), then run the resulting model through the same
   walk-forward/Monte Carlo validation from Phase 05 before trusting it as
   a signal source.

## Milestone

- [ ] A second, uncorrelated strategy running alongside the first — the
      actual test of whether you understand the craft or got lucky once.

**See also:** [signals.md](signals.md)
