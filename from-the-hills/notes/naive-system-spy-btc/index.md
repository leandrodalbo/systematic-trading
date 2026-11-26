---
title: A naive system on SPY and BTC
description: "Buy after 3 days in a row: a tiny gain on SPY, a tiny loss on BTC. No real edge, but a good first lesson in testing a system."
date: 2026-11-26T13:39:00Z
picture: chart.png
pictureWidth: 1950
pictureHeight: 1050
pictureAlt: "Four lines in two panels, SPY and BTC. Top: account return, all runs end between −1% and +4%. Bottom: drawdown, never deeper than −4.5%."
---

## The question

Howard Bandy, *Mean Reversion Trading Systems*, chapter 3, starts with a naive system: buy after
a few days in the same direction. Does it work?

## Checked

- SPY 2016–2026 and BTC 2021–2026, daily prices (Alpaca).
- Buy at the close after 3 days in a row:
  - **up** = [trend following](/concepts/reading-a-backtest/)
  - **down** = [mean reversion](/concepts/reading-a-backtest/)
- Sell at the stop, the target, or after 7 days, whichever comes first.
- SPY: stop 1%, target 2%. BTC moves about 3 times more, so stop 3%, target 6%.
- 100,000 account, 10,000 per trade, one trade at a time, no costs.

## What the data said

| | SPY up | SPY down | BTC up | BTC down |
|---|---|---|---|---|
| Trades | 211 | 185 | 143 | 192 |
| Winners | 50% | 45% | 36% | 36% |
| Average trade | +0.19% | +0.16% | −0.04% | −0.04% |
| [CAR](/concepts/reading-a-backtest/) | 0.37% | 0.27% | −0.10% | −0.13% |
| [Max drawdown](/concepts/reading-a-backtest/) | 1.54% | 1.40% | 3.79% | 4.50% |
| [Exposure](/concepts/reading-a-backtest/) | 35% | 19% | 20% | 28% |

Top: account return. Bottom: drawdown.

## What it means

- **No real edge.** SPY gains a little, most likely just because SPY rose over these years.
  BTC loses a little.
- **Buying after falls works harder.** SPY "down" makes less in total, but is in the market on
  fewer days (19% against 35%), so each day earns more. Its wins are bigger too (+1.73% against
  +1.39%).
- **Levels must fit the market.** With 1% and 2%, BTC often hit both on the same day, and the
  test can't tell which came first. 3% and 6% fixed it.

## Limits

- No trading costs.
- Few trades, so luck plays a big part.
- Past data. The future may differ.

[The script on GitHub](https://github.com/leandrodalbo/systematic-trading/blob/main/implementations/data-analysis/scripts/naive_sequence.py)
