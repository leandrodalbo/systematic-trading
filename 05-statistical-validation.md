# Phase 05 — Statistical Validation

**Goal:** find out if the system is real or if you curve-fit it to years of
noise. This is the phase most self-taught traders skip — don't skip it.

## Learn

- In-sample vs. out-of-sample split, walk-forward optimization
- Monte Carlo resampling of trades to estimate a range of plausible outcomes
- Multiple-testing bias — the more parameter combinations you try, the more
  you must distrust the best-looking one

## Read

- *Evidence-Based Technical Analysis* — David Aronson —
  [find it on Amazon](https://www.amazon.com/s?k=Evidence-Based+Technical+Analysis+David+Aronson)

## Steps

1. Split your Phase 04 data into in-sample (e.g. first 70%) and
   out-of-sample (last 30%). Re-tune parameters only on in-sample data.
2. Run the tuned system on out-of-sample data. Compare the stats — a
   material drop in edge means overfitting.
3. Implement a simple walk-forward loop: tune on a rolling window, test on
   the next unseen window, roll forward.
4. Run a Monte Carlo resample of your trade list (shuffle trade order,
   resample with replacement) to get a distribution of possible outcomes,
   not just one lucky sequence.

## Milestone

- [ ] Your system survives a walk-forward test on data it never saw during
      design, without a material drop in edge.

**Next:** [06-risk-position-sizing.md](06-risk-position-sizing.md)
