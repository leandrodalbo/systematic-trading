---
title: Distribution and its four moments
description: "A distribution shows how often each result happens. Four numbers describe its shape: mean, std dev, skewness, kurtosis."
date: 2026-11-23T19:15:00Z
---

## What it is

- **Distribution:** how often each value happens. For example: how many trades won 1%, how many
  lost 2%, and so on.
- **Bell curve (normal distribution):** the textbook shape. Most values close to the average, both
  sides the same, and extreme values get rare very fast:
  - 68% of values within 1 std dev of the mean.
  - 95% within 2.
  - 99.7% within 3. So a value beyond 3 std devs: about 1 in 370.

## The four moments

Four numbers that describe the shape:

1. **Mean:** the average value. Where the middle sits ([mean vs median](/concepts/mean-vs-median/)).
2. **Std dev:** how far a typical value is from the mean. Bigger = more spread out.
3. **Skewness:** which side the long tail is on. 0 = both sides the same. Below 0 = long tail on
   the left (extreme values more often low). Above 0 = long tail on the right (more often high).
4. **Excess kurtosis:** how far the extreme values go from the mean, compared to a bell curve.
   0 = same as a bell curve. Above 0 = extreme values go further out. How far counts much more
   than how many.

## How to read it

- Skewness = which side. Kurtosis = how far. A count beyond 3 std devs = how many.
- For returns, skewness below 0 and high kurtosis together: rare, big losses that a bell curve
  doesn't expect. The risk to watch.

## Example

Daily returns, SPY 2016–2026 and BTC 2021–2026:

- Std dev: SPY 1.10%, BTC 2.98%. A typical SPY day moves about 1%, a BTC day about 3%.
- Skewness: SPY −0.36, the long tail is on the drops. BTC +0.16, leans slightly to rises.
- Days beyond 3 std devs: SPY 40, BTC 41. A bell curve expects about 7 and 6. Almost the same
  count.
- Excess kurtosis: SPY 12.5, BTC 4.0. SPY's worst day (−10.8%) was about 10 std devs out. BTC's
  worst were about 5. Same count, but SPY's go much further.

## Where it's used

- [Daily returns are not a bell curve](/notes/returns-not-bell-curve/)
