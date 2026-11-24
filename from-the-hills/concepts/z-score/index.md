---
title: What is a z-score?
description: "How far a price is from its average of the last N days, counted in its own typical moves."
date: 2026-11-24T19:00:00Z
---

## What it is

1. Take the last N closing prices. The number says how many: z20 = 20 days, z50 = 50 days.
2. Work out their mean (the average) and their
   [std dev](/concepts/four-moments/) (the size of a typical move away from the mean).
3. z = (today's close − mean) ÷ std dev.

The answer is in std devs, not percent. "z −2" means the price is 2 typical moves below its
average. The weekly market check uses z20.

## How to read it

- **0:** right on the average.
- **Between −1 and +1:** a normal day.
- **Below −2 or above +2:** stretched. On a bell curve, about 1 day in 20 (both sides together).
- The window sets the view: z20 is short-term. For the long trend, look at the
  [200-day average](/concepts/200-day-average/).

## Why not just percent

Every ticker has its own normal move. The same drop in percent can be normal for one and extreme
for another. The z-score measures each one against its own swings.

## Example

Closing prices on 20 November 2026:

- **SPY 689.81.** 20-day mean 747.81, std dev 20.53.
  z20 = (689.81 − 747.81) ÷ 20.53 = **−2.83**. The price was 7.8% under its average.
- **BTC 58,818.** 20-day mean 71,013, std dev 6,449. The price was 17.2% under its average, more
  than twice SPY's drop, but z20 was only **−1.89**. For BTC, big swings are normal.
- SPY closed at z20 −2.83 or lower on 17 of 2,719 days since 2016: about 1 day in 160.
- The next trading day, SPY rose 4.7% and z20 went back to −1.12.

## Where it's used

- The weekly market check: the z20 number ([20 November 2026](/market/2026-11-20/)).
