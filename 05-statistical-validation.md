# Phase 05 — Statistical Validation

**Goal:** find out if the system is real or curve-fit to noise. Most self-taught traders skip this phase. Don't.

## Learn

- In-sample vs. out-of-sample, walk-forward optimization
- Monte Carlo resampling of trades for a range of plausible outcomes
- Multiple-testing bias: the more parameter combos you try, the less you trust the best one

## Read

- *Evidence-Based Technical Analysis* — David Aronson — [Amazon](https://www.amazon.co.uk/s?k=Evidence-Based+Technical+Analysis+David+Aronson). ~559 pages, ~15 pages/hr (dense) ≈ **37 days**, the biggest block in the curriculum. Overlap it with the Phase 04 build. If short on time, the back half (data-mining bias, out-of-sample testing) matters more here than the early chapters on subjective chart-reading.

## Free tools

- Python: the Phase 04 backtest runs the walk-forward (re-tune, test, roll forward); `numpy`/`scipy` do the Monte Carlo and significance tests.

## Steps

1. Split the data: in-sample (e.g. first 70%), out-of-sample (last 30%). Tune only on in-sample.
2. Run the tuned system out-of-sample. A material drop in edge means overfitting.
3. Walk-forward loop: tune on a rolling window, test on the next unseen window, roll forward.
4. Monte Carlo the trade list (shuffle order, resample with replacement) to get a distribution of outcomes, not one lucky sequence.

## Milestone

- [ ] Your system survives a walk-forward test on data it never saw during
      design, without a material drop in edge.

**Next:** [06-risk-position-sizing.md](06-risk-position-sizing.md)
