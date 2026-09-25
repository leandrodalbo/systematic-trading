# Backtesting

## What a backtest is

- Testing a strategy on past prices, as if you were trading back then — using only what you could have known on each day.

- It's good at showing a strategy is bad. It can't prove a strategy is good.

## Before you backtest, ask

- **Does it fit me?** Do I have the money and the time to run it?

- **Were costs included?** If someone's results ignore fees, don't trust them.

- **Does it still work?** Look at the last few years, not only the long-term average. Strategies stop working once many people use them.

## Data

- **Adjusted prices:** prices must be corrected for splits and dividends. Otherwise a 2-for-1 split looks like a 50% crash.

- **Survivorship bias:** if your data only has companies that still exist today, you miss the ones that went bust. Results look better than they really were.

## Measuring results

- **Sharpe ratio:** how much you earn for the risk you take (return ÷ how much returns jump around). Below 1 is weak, above 2 is good.

- **Max drawdown:** the biggest drop from a high point to a low point.

- **Drawdown duration:** how long it took to get back to the high point. Waiting a long time is often harder than the drop itself.

## Common mistakes

- **Look-ahead bias:** using information you wouldn't have had yet. Example: using today's closing price to decide a trade you make today.

- **Data snooping (overfitting):** tweaking the strategy until the past looks perfect. It fits random luck, not a real pattern. How to avoid it:
 
  - Keep rules simple, with few settings.
  - Test on lots of data (many trades).
  - Tune on one part of the data, then test on a part you never touched. Paper trading is the final test.
 
  - Change each setting a little. If results stay good, the strategy is solid. If they fall apart, it was overfitted.

- **Ignoring costs:** every backtest must include them:
  - Fees (Alpaca: small fees on stock sales, 0.15–0.25% per crypto trade).
  - Slippage: the price you get is worse than the price you saw.
  - Market impact: big orders push the price against you.
  - The more often you trade, the more costs hurt.

## Why real trading does worse than the backtest (Phase 01 milestone)

- **Look-ahead bias:** the backtest used information you won't have live.

- **Overfitting:** the strategy learned past luck, and luck doesn't repeat.

- **Survivorship bias:** the backtest only traded companies that survived.

- **Costs:** fees and slippage were ignored or too low.

- **The market changed:** conditions shifted, or too many people started using the same idea.
