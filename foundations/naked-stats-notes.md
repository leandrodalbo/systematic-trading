# Naked Statistics — cheat sheet

## Descriptive stats

- **Mean** = average. **Median** = middle value. Close together unless
  outliers skew the mean.
  
- **Std dev** = how spread out data is around the mean — not where the
  center is. Two strategies can share a mean return with very different
  std devs; the std dev is often the more important number (closer to
  "risk").
- **Normal distribution** = symmetric bell curve, mean = median.

## Probability & sampling

- Probability = studying events/outcomes involving uncertainty.
- Independent events: P(A and B) = P(A) × P(B). Mutually exclusive:
  P(A or B) = P(A) + P(B) (otherwise subtract the overlap).
- **Law of Large Numbers**: more trials → observed average converges on
  the true expected value.
- **Central Limit Theorem**: sample means cluster normally around the
  population mean; bigger samples → closer to a true normal curve. Lets
  you judge whether a sample plausibly came from a given population — the
  basis for sampling-based tests even when raw data isn't normal.
- **Standard error** = dispersion of the sample *means* themselves (not
  individual data points — that's std dev).

## Correlation & regression

- **Correlation** = degree to which two things move together (e.g. temp ↔
  ice cream sales). Correlation coefficient (r) puts a number on strength
  + direction.
- **Correlation ≠ causation.** A third factor, or pure chance, can explain
  it. Test enough variables/strategy variants and some will correlate with
  returns by coincidence — the root of overfitting.
- **Regression** = best-fit line quantifying a relationship while
  controlling for other factors. Still association, not mechanism — a
  coefficient isn't proof of a causal lever.

## Inference & pitfalls

- **Inference** = best guess at the explanation behind a pattern, via
  probability. Not guaranteed correct.
- **Statistical inference** = the formal version: drawing conclusions from
  a sample rather than just describing it.
- **"Statistically significant" ≠ "will keep working."** Significance is
  about how surprising a result would be by chance — not about effect size
  or future durability.
- **Selection / survivorship bias**: judging by survivors only (listed
  stocks, still-running funds, strategies that made the backtest universe)
  inflates how good things looked in advance.

## Formulas (quick reference)

| What | Formula |
|---|---|
| Mean | x̄ = Σxᵢ / n |
| Variance | σ² = Σ(xᵢ − x̄)² / n |
| Std dev | σ = √variance |
| Z-score | (x − μ) / σ |
| Correlation (r) | cov(x, y) / (σₓ · σᵧ), range −1 to 1 |
| Sharpe ratio | (mean return − risk-free rate) / std dev of returns |

## Why it matters (Phase 00 milestone)

A backtest Sharpe ratio can mislead because: small samples inflate
apparent significance, testing many strategy variants manufactures
spurious correlation (data snooping), and survivorship/selection bias in
the backtest universe overstates results.

## Next

Stats appendix of *Quantitative Trading* (Chan) — apply this by hand,
recompute a couple of the example stats.
