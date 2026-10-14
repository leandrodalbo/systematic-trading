# Phase 00 — Foundations

**Status: completed.**

**Goal:** just enough math to read a strategy's statistics honestly.

## Learn

- Probability & distributions, mean/variance/standard deviation
- Correlation vs. causation, stationarity, autocorrelation
- Prices vs. returns, log returns

## Read

- *Naked Statistics* — Charles Wheelan — **finished** — [Amazon](https://www.amazon.co.uk/s?k=Naked+Statistics+Charles+Wheelan). ~256 pages, ~30 pages/hr ≈ **9 days**.

Chan's *Quantitative Trading* has no stats appendix (it's a MATLAB primer). Its stats content (Sharpe, backtest significance) is in Chapter 3, covered in Phase 01.

## Steps

1. Read *Naked Statistics* end to end.
2. In your own words: what does "statistically significant" mean, and why doesn't it mean "will keep working"?

Statistical significance means a result probably wasn't just random luck,
based on the past data you tested it on. It does not mean the pattern will
keep happening — it says nothing about overfitting, regime change, or
whether the effect is even big enough to matter. So "statistically
significant" answers "was this likely chance?", not "will this keep
working?"

## Milestone

- [x] You can explain, in one paragraph, why a strategy's backtest Sharpe
      ratio can be misleading.

A backtest Sharpe ratio can look really good even when the strategy isn't actually good.


1. What's a Sharpe ratio?

The Sharpe ratio is a way of asking:

"How much return did I get compared with how much risk I took?"

For example:

Strategy A makes 10% with relatively little ups and downs → Sharpe might be 1.5
Strategy B makes 10% but has huge ups and downs → Sharpe might be 0.5

So, generally, higher Sharpe = better risk-adjusted historical performance.

A backtest means you're testing the strategy on old historical data.
A high backtest Sharpe ratio doesn't guarantee a strategy is actually good.

Luck: Good results can happen by chance.

Overfitting: If you test lots of strategies and pick the best one, it may only look good because it fit the past.

Survivorship bias: If you only test companies that survived, you ignore the failures.

In short: A great backtest can sometimes be luck, not skill.

## Parallel track: manual investing (optional)

Separate from the systematic project: research and buy single stocks by hand. Keep this money fully separate from anything the system will trade.

- **Broker: XTB** — FCA-regulated, free stock/ETF dealing at normal volumes, easiest UK app to start with.
- **Read:** *Advanced Stocks Analysis – A Fundamentalist Approach* — Luciano Storelli — [Amazon](https://www.amazon.co.uk/dp/B0C5YQLBZ5). Fundamental analysis (financial statements, valuation), not quant.
- **Steps:** open the account, read the book, research 1–2 companies, buy a small position if convinced. No automation, no backtest: the point is the fundamental-analysis skill the systematic side doesn't teach.

**Next:** [01-market-mechanics.md](01-market-mechanics.md)
