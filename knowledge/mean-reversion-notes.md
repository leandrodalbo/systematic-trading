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
