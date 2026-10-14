# Systematic Trader

A self-directed curriculum from statistics to a validated mean-reversion swing-trading system for stocks and crypto, with free tools only. Python for the strategy (signals, backtest), Kotlin + Spring Boot for data and orders, Alpaca for both markets: see [foundations/architecture.md](foundations/architecture.md).

Final project: **Mean Reversion Trading System**, based on Howard Bandy's book of the same name. Read it for the ideas; reimplement the logic yourself instead of using his AmiBroker/AFL code.

## How to use this folder

One file per phase. Work through it, tick the milestone, move on. Each book has an Amazon search link (pick paperback/Kindle/used yourself); check the library or O'Reilly first.

- [00-foundations.md](00-foundations.md) — done
- [01-market-mechanics.md](01-market-mechanics.md) — done
- [02-programming-data-pipeline.md](02-programming-data-pipeline.md)
- [03-strategy-families.md](03-strategy-families.md)
- [04-build-the-system.md](04-build-the-system.md) — the final project
- [05-statistical-validation.md](05-statistical-validation.md)
- [06-risk-position-sizing.md](06-risk-position-sizing.md)
- [07-paper-trading-live.md](07-paper-trading-live.md)
- [08-infrastructure.md](08-infrastructure.md)
- [09-specialize.md](09-specialize.md)
- [signals.md](signals.md) — how you'll know it's working
- [foundations/](foundations/) — notes and cheat sheets

## Two directions

- **Practitioner (this guide):** build, validate and run your own systems with your own capital. Self-taught, engineering-led, realistic with a backend background and no finance degree.
- **Quant researcher/developer:** hired by a fund or prop shop. Usually needs strong stochastic calculus/stats or competitive programming (for HFT). Not the focus, but Phases 00–05 overlap almost fully.

## Timeline (part-time, ~5–10 hrs/week)

| Phases | What | Duration |
|---|---|---|
| 00–03 | Foundations, market mechanics, data pipeline, strategy theory | 5–7 weeks |
| 04 | Build the mean-reversion system | 3–4 weeks |
| 05 | Statistical validation (walk-forward, Monte Carlo) | 2–3 weeks |
| 06 | Risk & position sizing | 1 week |
| 07 | Paper trading | **3 months minimum, wall-clock** (the market must produce enough trades) |
| 08 | Infrastructure (parallel with 07) | 1–2 weeks |
| 09 | Specialize | ongoing |

**Realistic total: 6–9 months** part-time to a running, validated system with a paper track record. That's a working system, not mastery: expect 1–2 more years of iteration (more strategies, surviving a drawdown, refining risk) before "professional" stops feeling like a stretch. Phase 05 is the one most self-taught traders skip, and skipping it is the main reason great backtests lose money live.

Reading: each phase lists page counts and days at ~1 hr/day. **~124 days total (~4 months)**, overlapping the build and validation work, not added to it.
