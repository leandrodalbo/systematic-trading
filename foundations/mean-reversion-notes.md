# Mean Reversion Systems (Howard Bandy)

Notes so far: chapter 1 to the middle of chapter 2.

## Ways a system decides to enter

- **Trend following:** buy after the price has already started going up.
- **Mean reversion:** buy when the price has moved far from its normal level, expecting it to come back.
- **Seasonality:** buy because of a calendar pattern (day of week, month).
- **Pattern:** buy when a specific price pattern shows up (e.g. 3 lower closes in a row).
- **Cycles:** buy at the low point of a detected cycle.

## Mean reversion

- A price that moves a lot away from its average tends to come back.
- Example: stocks with the highest P/E one year tend to have lower P/E the next.
- **Swing trading:** active trading, holding for 1 day to a few days. This is the book's focus.
- Usually traded on ETFs, e.g. **SPY** (a fund that tracks the S&P 500).

## Watch out for the upward bias

- Stocks have mostly gone up over the long run because of resource extraction, population growth, inflation, productivity gains, survivor bias and legislation.
- So a long-only system can look good just from riding that drift.
- Test over periods that include falling and flat markets, not only bull markets.

## Technical vs quantitative analysis

- **Technical:** price and volume hold all the information you need. Look for patterns in them.
- **Quantitative:** same data, but:
  - no vague chart patterns
  - precise math indicators and models
  - statistical checks on the results

## A trading system

- **Model:** the rules, logic and parameters.
- **Data:** the price series.
- **Signals:** the buy/sell points the model produces when it finds its pattern in the data.
- A model is a fixed picture of a market that keeps changing. While they stay in sync, the system makes money. When they drift apart, it stops working.
- Math rules are objective. Chart reading is not.

## Building and validating a system

1. **Pick an objective function:** one number that ranks the alternatives (what "better" means to you).
2. **Design the system:** model + data.
3. **In-sample:** the data you use to build and tune the system.
   - Trade-off: a short window stays in step with the current market but has fewer trades. A long window has more trades but mixes in old conditions. *(to confirm in later chapters)*
4. **Optimise:** try alternatives and keep the best by the objective function.
5. **Out-of-sample:** test on data the system has never seen. These results are the best guess of the future.
6. **Walk forward:** repeat steps 3–5, moving the windows forward each time. Join all the out-of-sample results together.
7. **Position size:** set it from the out-of-sample results (expected drawdown and profit).
8. **Monitor health:** if live results drop below what the stats expect, cut the position size.
