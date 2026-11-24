# Quant Research Studio: the reading plan

A self-taught path from statistics to a tested mean-reversion swing-trading system for stocks and
crypto, with free tools only. Python for the strategy (signals, backtest), Kotlin + Spring Boot
for data and orders, Alpaca for both markets: see [knowledge/architecture.md](knowledge/architecture.md).

Final project: **Mean Reversion Trading System**, based on Howard Bandy's book of the same name.
Read it for the ideas; rebuild the logic yourself instead of using his AmiBroker/AFL code.

Site: [quantresearch.studio](https://quantresearch.studio). Plan: [STUDIO.md](STUDIO.md). Read
every day, check the market every Saturday, and build, test or post when something comes up.
Daily notes in [journal/](journal/).

## How to use this folder

One file per phase, in reading order. Read a chapter a day; don't wait to finish a book or a phase
before building, testing or posting. Tick the milestone when it's done. Each book has an Amazon
search link; check the library or O'Reilly first.

- [00-foundations.md](00-foundations.md) — done
- [01-market-mechanics.md](01-market-mechanics.md) — done
- [02-programming-data-pipeline.md](02-programming-data-pipeline.md) — done
- [03-strategy-families.md](03-strategy-families.md) — in progress
- [04-build-the-system.md](04-build-the-system.md) — the final project
- [05-statistical-validation.md](05-statistical-validation.md)
- [06-risk-position-sizing.md](06-risk-position-sizing.md)
- [07-paper-trading-live.md](07-paper-trading-live.md)
- [08-infrastructure.md](08-infrastructure.md)
- [09-specialize.md](09-specialize.md)
- [signals.md](signals.md) — how you'll know it's working
- [knowledge/](knowledge/) — notes and cheat sheets

## Pace

Each phase goes at its own pace. Two fixed things:

- **Phase 05 (validation) is never skipped.** Skipping it is the main reason great backtests lose
  money live.
- **Phase 07 needs at least 3 months of paper trading,** so the market can produce enough trades.
