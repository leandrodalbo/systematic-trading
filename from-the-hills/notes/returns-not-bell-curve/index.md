---
title: Daily returns are not a bell curve
description: "Big daily moves happen about 6 times more often than a bell curve says."
date: 2026-11-23T18:47:00Z
picture: chart.png
pictureWidth: 1800
pictureHeight: 675
pictureAlt: "Two histograms of daily returns on a log scale, SPY and BTC, each with a bell curve on top. Both have a taller middle and many more big days than the curve."
---

## The question

Many models expect daily returns to fall within a bell curve (normal distribution): most days
close to the average, and very big days almost never. Is that true for SPY and BTC?

## Checked

- SPY daily closes, 2016–2026, and BTC daily closes, 2021–2026 (Alpaca).
- Daily return = change from one close to the next. 
- The [four moments](/concepts/four-moments/) of each: mean, std dev, skewness, excess kurtosis.
- Very big days (more than 3 times a typical day's move): how many there were, and how many a
  bell curve expects.

## What the data said

| | SPY | BTC | Bell curve |
|---|---|---|---|
| Days | 2,737 | 2,151 | |
| Mean | +0.06% | +0.08% | |
| Std dev | 1.10% | 2.98% | |
| Skewness | −0.36 | +0.16 | 0 |
| Excess kurtosis | 12.5 | 4.0 | 0 |
| Very big days | 40 | 41 | 7 / 6 |

More quiet days, and more huge days, than a bell curve says. True for both.

SPY: huge days are more often drops. Calm most of the time, then a shock (−10.8% in one day,
16 March 2020, the COVID crash).

BTC: jumpy every day, so its huge days surprise less.

## What it means

A risk number built on a bell curve expects too few big days. Better to use the real
distribution of returns, or of trades.

## Limits

- One stock fund and one coin.
- 11 years of SPY, 6 of BTC.
- Past data. The future may differ.

[The script on GitHub](https://github.com/leandrodalbo/systematic-trading/blob/main/implementations/data-analysis/scripts/return_distribution.py)
