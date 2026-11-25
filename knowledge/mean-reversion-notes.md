# Mean Reversion Systems (Howard Bandy)

## Day 1

### Goal

- We want to build systems a trader can trust and that will perform well.
- Trust comes from four things: validation, understanding, distribution of profit, distribution of drawdown.
  - **Distribution of profit:** a backtest gives one result, but the future can play out in many
    ways. Bandy shuffles the trades into many random orders and asks: "in 95 out of 100 possible
    futures, how much did I make?" You get a range, not one number.
  - **Distribution of drawdown:** the same idea for the worst drop: "in 95 out of 100 futures, my
    worst drop stayed under X%."
- Success is decided by statistical evidence, not by a good feeling.

### Trading system types

**1) First classification: what the rule is based on**

- **Trend-following:** buy when prices have already started going up.
- **Mean-reverting:** buy when prices are unusually low, expecting them to go back to normal.
- **Time and patterns** (a separate group, not part of mean reversion):
  - Seasonality (calendar effects, e.g. end of month).
  - Patterns.
  - Cycles.

**2) Second classification: the position**

- **LONG:** you own it; you make money when the price goes up.
- **SHORT:** you borrow and sell it; you make money when the price goes down.
- **FLAT:** you hold nothing (cash).
- Note: on Alpaca, crypto can't be shorted (see [stocks-vs-crypto-notes.md](stocks-vs-crypto-notes.md)).

**Swing trades:** last from one day to a few days. Short holding periods.

### Interesting things to research (checked 2026-11-21)

Data: daily prices from Yahoo, SPY 1993–2026, QQQ 1999–2026.
No trading costs. "Rising market" = price above its 200-day average.

**1. Prices act differently when rising than when falling. In rising markets, drops are steeper and shorter.**
**Verdict: true.**

- SPY in a rising market, swings of 5% or more (median):
  - Drops: 17 days long, −0.49% a day.
  - Rises: 56 days long, +0.26% a day.
  - So drops are about 3× shorter and 2× steeper.
- QQQ shows the same (drops 14.5 days, rises 28 days).
- In a falling market (price below the 200-day average), daily moves are twice as big:
  SPY 1.89% a day vs 0.86% in a rising market. Rebounds are fast and sharp too.
- Why it matters: in a rising market, short sharp dips are the ones to buy. This is the base for
  the RSI(2) idea with a 200-day filter.

**2. Stocks with a high P/E one year tend to have a lower P/E the next year.**
**Verdict on Apple: loosely true, but the "normal" P/E moved.**

- P/E = price ÷ earnings per share (EPS). It's how many dollars you pay for $1 of yearly profit.
  P/E 38 = you pay $38 for each $1 Apple earns in a year.
- Apple, fiscal years 2007–2026 (EPS from SEC EDGAR, Apple's own filings; price from Yahoo at each
  year end, late September). Median P/E: 19.1.
  - After a high-P/E year (above 19.1), P/E fell the next year 6 times out of 9 (median −6%).
  - After a low-P/E year, P/E fell only 3 times out of 10 (median +8%).
- P/E can fall in two ways: the price drops, or the earnings catch up.
  - 2007 → 2008: 39 → 19. The price dropped 16% (the 2008 crash) and EPS grew 71%.
  - 2020 → 2021: 34 → 26. The price went **up** 31%, but EPS grew 71%. Earnings caught up.
  - 2019 → 2020: 18 → 34. The price doubled while EPS grew only 10%.
- The catch: Apple's "normal" changed. P/E was 12–19 from 2011 to 2019, and 25–38 since 2020.
  Anyone who sold in 2020 because "P/E 34 is too high, it will come back to 19" missed a big
  rise. Mean reversion needs a stable mean, and here the mean moved.
- Now: P/E 38.0 at the end of FY2026 (EPS $8.98). With today's price ($342.56, 20 Nov), about 38.
- One stock proves nothing. The claim is about many stocks; that test comes later.


- Why a high P/E can come down: a bad earnings year pushes P/E up; when earnings recover, P/E
  falls back. So part of it is just earnings bouncing around.
- Research behind it: Basu (1977), low-P/E stocks beat high-P/E stocks; De Bondt and Thaler (1985),
  the worst stocks of the last 3–5 years beat the best ones over the next few years.
- Test later in the "Fundamentals corner" (SEC EDGAR data). Watch for survivorship bias: companies
  that went bust drop out of the data.
- Look at it by hand now: see "How to check it yourself" below.

### How to check it yourself

**TradingView (prices, free account is enough)**

1. Open tradingview.com, search `SPY`, open the chart, set the time frame to **1D** (daily).
2. Add indicators (the "Indicators" button):
   - **Moving Average**, length 200: above it = rising market, below it = falling market.
   - **Bollinger Bands**, length 20, StdDev 2: price under the lower band = about 2 standard
     deviations below the 20-day mean (the z-score idea).
   - **RSI**, change length to 2: below 10 = very oversold (Connors).
3. Claim 1: scroll back through a rising period and compare how many bars the drops take vs the rises.

**stockanalysis.com (company numbers, for the P/E claim)**

1. Open <https://stockanalysis.com/fundamental-chart/>.
2. Pick a company (e.g. AAPL) and a number (e.g. **PE Ratio**).
3. Look at it over many years: when P/E was very high one year, what happened the next?
4. Try a few companies that had a big earnings drop and see if P/E jumped and then came back.

### ETFs

- Most are built to follow an index.
- Very liquid: SPY (S&P 500), QQQ (Nasdaq 100), IWM (Russell 2000, small companies), EEM
  (emerging markets), GLD (gold).
  - The book list said IWN: that is the Russell 2000 *Value* fund. Check which one Bandy means.
- Sector funds:
  - XLB materials
  - XLE energy
  - XLF financials
  - XLI industrials
  - XLK technology
  - XLP consumer staples (food, household goods)
  - XLU utilities
  - XLV health care
  - XLY consumer discretionary (things people buy when they have spare money)

### KISS

Keep it simple and straightforward.

## Day 2 (2026-11-23): chapter 2

### Technical vs quantitative

- Both believe some patterns come before good trades.
- Technical: reads charts by eye.
- Quantitative: exact rules, testable, results checked with statistics.

### Model + data

- Model: the rules and parameters. It finds the pattern.
- Data: the prices. Signal + noise.
- The model is fixed, the market changes. While they stay in sync, the system works.

### Objective function

- One number to rank systems from best to worst.

### The steps

1. In-sample: data to build and tune the model. Short.
2. Optimise: try options, keep the best. Best in-sample can be luck.
3. Out-of-sample: new data the model never saw. The honest test.
4. Walk-forward: repeat 1–3, moving forward in time.
5. Best estimate trades: all out-of-sample trades together. Used to judge the system.
6. Position size: a separate step, from the best estimate trades.
7. Trade the signals.
8. Monitor: results drop → trade smaller or stop.
9. Resync: run the walk-forward again.


### Distributions

- Use distributions, not single numbers.
- Population: all future trades. Can't be known in advance.
- Sample: the trades we have (backtest). Used to estimate the population.
- Goal: learn as much as possible about the future trades and estimate performance.
- List of trades: gain/loss per trade, holding days, intra-trade drawdown, how often it trades.
- Distribution from any trade metric:
  - put each trade in a bin (e.g. 0% to +1%)
  - count trades per bin
  - chart: bins vs count (histogram)

### Four moments (the shape)

- Mean: the average trade.
- Variance: spread around the mean. Std dev = √variance, same units as the trades.
- Skewness: how asymmetric (lopsided) the distribution is around the mean. Long left tail = rare big losses.
- Kurtosis: "tailedness", how prone to extreme values (outliers) vs a normal distribution. High = rare big surprises. Bandy says "peakedness": old view, it's about the tails.

- Mean and std dev: the usual way to describe trade results.

### Monte Carlo

- Build thousands of random trade sequences from the best estimate trades.
- Get distributions of: account growth, max drawdown, win-to-loss ratio.

### Validation

- Walk-forward gives the best estimate trades: the best guess of future performance.

### Risk

- Risk = % drawdown from the account's highest balance.
- Intra-trade drawdown counts too, not only closed trades.
- Every trade has an exit rule that closes it.
- Risk tolerance is personal. Bandy's form: "5% chance of a 20% drawdown over 2 years".

### Position size and profit

- Position size: separate from the system, from the best estimate trades + risk tolerance.
- safe-f: biggest fraction of the account per trade that stays inside the risk tolerance.
- CAR25: yearly growth at the 25th percentile with safe-f. Bandy's objective function.

### System health

- Health changes as the data changes and the model drifts in or out of sync.
- Dynamic: add each new trade to the set, reassess risk, adjust position size.

### Iterative design

- Change logic and parameters → test → repeat.

## Day 3 (2026-11-25): chapter 3

### Naive system: buy after an n-day sequence

- Traded: SPY.
- Data: 1 Jan 1999 to 1 Jan 2012.
- Indicator: a run of rising or falling days.
- Action: at the close of the signal bar.
- Long only.
- Objective function: CAR/MDD.
  - CAR: compound annual return, the yearly growth rate.
  - MDD: maximum drawdown, the biggest drop from a high.
  - CAR/MDD: growth per unit of worst drop. Higher is better.
- Initial equity: 100,000.
- Open positions: 1 at most (10,000 each).

### Parameters

- `n_days_consecutive`: how many days in a row.
- `direction`: rising or falling.
- `holding_days`: most days to hold.
- `profit_target`: % gain that closes the trade.
- Example: (3, 0, 7, 05).

### Rules

- Buy: N days in a row in the same direction.
- Sell: after the max holding days, or at the profit target.

### Discussion

- Long after rising days: trend following.
- Long after falling days: mean reversion.

### Summary

**Account**

- Initial capital.
- Ending capital.
- Net profit, net profit %.
- Exposure: % of time in the market.
- Net risk-adjusted return: net profit % ÷ exposure.
- Annual return.

**All trades**

- Avg profit/loss, avg profit/loss %.

**Winners**

- Total profit.
- Avg profit, avg profit %.
- Largest win.

**Losers**

- Total loss.
- Avg loss, avg loss %.
- Largest loss.
