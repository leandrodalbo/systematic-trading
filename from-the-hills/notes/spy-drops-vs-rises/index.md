---
title: Drops vs rises in SPY, 1993–2026
description: "SPY above its 200-day average, 1993–2026: drops lasted 17 days, rises 56. Drops moved about twice as fast."
date: 2026-11-21T13:30:00Z
chart: chart.png
chartWidth: 1170
chartHeight: 572
chartAlt: "Bar chart. SPY above its 200-day average: drops last 17 days at 0.49% a day, rises last 56 days at 0.26% a day."
---

## The question

Howard Bandy writes that prices act differently when they rise than when they fall: in a rising
market, drops are steeper and shorter. Is that true for SPY?

## How I checked

- SPY daily prices from 1993 to 2026 (Yahoo).
- "Rising market" = price above its 200-day average.
- I cut the prices into swings of 5% or more, up or down, and measured each one: how many days
  it lasted and how much it moved per day.

## What the data said

In a rising market (medians):

- **Drops:** 17 trading days, −0.49% a day.
- **Rises:** 56 trading days, +0.26% a day.

So drops are about **3× shorter** and **2× faster** than rises. QQQ shows the same (drops 14.5
days, rises 28).

In a falling market (price below its 200-day average), daily moves are about twice as big:
1.89% a day against 0.86%.

## What it means

In a rising market, dips are short and sharp. That is the idea behind mean reversion systems
that buy quick dips, but only while price is above its 200-day average.

## Limits

- No trading costs.
- One fund, past data.
