---
title: Reading a backtest
description: "The words a system test uses: CAR, max drawdown, CAR/MDD, exposure, stop and gap, trend following and mean reversion."
date: 2026-11-26T13:50:00Z
---

A backtest runs a strategy on past prices, as if trading back then, using only what was known
each day. It can show a strategy is bad, but it can't prove one is good (Ernest Chan,
*Quantitative Trading*).

Terms from Howard Bandy, *Mean Reversion Trading Systems*, chapter 3. Stop and gap are extra.

## CAR

Compound annual return: how fast the account grew each year, on average.

- **Higher is better.** Negative means the account shrank.
- Gains earn more gains: doubling in 10 years is **7.2% a year**, not 10%.

Example: SPY, buy after 3 up days. 100,000 → 104,056 in about 11 years = **0.37% a year**.

## Max drawdown (MDD)

The biggest fall from a high point before a new high. The worst stretch of the test.

Example: 100,000 → 120,000 → 90,000 → 130,000. Worst fall: 120,000 to 90,000 = **25%**.

- **Smaller is calmer.**
- Big falls are hard to win back: after −50%, it takes +100%.

The SPY system: **1.54%**.

## CAR/MDD

CAR ÷ MDD: growth for each unit of pain. Higher is better. Bandy uses it to compare systems.

SPY: 0.37 ÷ 1.54 = **0.24**.

## Exposure

The share of days with a trade open. 35% = about 1 day in 3.

- **Low exposure:** the money waits most of the time, so the system can look calm just because
  it's rarely in.
- Net profit ÷ exposure = what each day in the market earned (Bandy: net risk-adjusted return).

SPY:

- **After 3 up days:** 4.06% ÷ 35% = **0.12**
- **After 3 down days:** 3.03% ÷ 19% = **0.16**

Buying after falls made less in total, but more for each day in the market.

## Stop and gap

- **Stop:** sells when the price falls to a set level. A 1% stop on 10,000 loses about 100.
- **Gap:** the price opens far from the last close. If it opens below the stop, it sells at that
  lower price.


## Trend following and mean reversion

- **Trend following:** price went up, so buy. Up keeps going up.
- **Mean reversion:** price went down, so buy. It comes back to its usual level.

Mean reversion buys into weakness, so a tight stop can close the trade before the bounce.

SPY 2016–2026:

- **After 3 up days (trend):** 211 trades, 50% winners, average win +1.39%
- **After 3 down days (mean reversion):** 185 trades, 45% winners, average win +1.73%
