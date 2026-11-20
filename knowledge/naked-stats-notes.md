# Naked Statistics — cheat sheet

## Descriptive stats

- **Mean** = average. **Median** = middle value. Close unless outliers skew the mean.
- **Std dev** = spread around the mean, not where the center is. Two strategies can share a mean return with very different std devs; std dev is often the more important number (closer to "risk").
- **Normal distribution** = symmetric bell curve, mean = median.

## Probability & sampling

- Probability = studying uncertain events/outcomes.
- Independent: P(A and B) = P(A) × P(B). Mutually exclusive: P(A or B) = P(A) + P(B) (otherwise subtract the overlap).
- **Law of Large Numbers:** more trials → observed average converges on the true expected value.
- **Central Limit Theorem:** sample means cluster normally around the population mean; bigger samples → closer to normal. Lets you judge if a sample plausibly came from a population, even when raw data isn't normal.
- **Standard error** = spread of the sample *means* (not of data points; that's std dev).

## Correlation & regression

- **Correlation** = how much two things move together (temp ↔ ice cream sales). r gives strength + direction.
- **Correlation ≠ causation.** A third factor or chance can explain it. Test enough variants and some will correlate with returns by coincidence: the root of overfitting.
- **Regression** = best-fit line for a relationship, controlling for other factors. Still association, not proof of a causal lever.

## Inference & pitfalls

- **Inference** = best guess at the explanation behind a pattern, via probability. Not guaranteed.
- **Statistical inference** = drawing conclusions from a sample, not just describing it.
- **"Statistically significant" ≠ "will keep working."** It's about how surprising a result is by chance, not effect size or durability.
- **Selection / survivorship bias:** judging only survivors (listed stocks, live funds, strategies in the backtest universe) inflates how good things looked in advance.

## Formulas

| What | Formula |
|---|---|
| Mean | x̄ = Σxᵢ / n |
| Variance | σ² = Σ(xᵢ − x̄)² / n |
| Std dev | σ = √variance |
| Z-score | (x − μ) / σ |
| Correlation (r) | cov(x, y) / (σₓ · σᵧ), range −1 to 1 |
| Sharpe ratio | (mean return − risk-free rate) / std dev of returns |

## Why it matters (Phase 00 milestone)

A backtest Sharpe can mislead: small samples inflate apparent significance, testing many variants manufactures spurious correlation (data snooping), and survivorship/selection bias overstates results.

## Next

Chan's *Quantitative Trading* chapter 3 (Sharpe, backtest significance): recompute a couple of its example stats by hand.
