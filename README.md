# Systematic Trader

A self-directed curriculum from statistics foundations to a validated
mean-reversion swing-trading system, covering stocks and crypto, built with
free tools only (Python and/or Java/`ta4j`, no paid platform licences).

Final project: **Mean Reversion Trading System** (concept from Howard Bandy's
book of the same name — read for the ideas, reimplement the logic yourself
rather than using his AmiBroker/AFL code).

## How to use this folder

Each phase is its own file. Open the one you're on, work through it, check
off the milestone, move to the next. Every book has an Amazon search link
next to it (search, not a specific listing, so you can pick paperback/
Kindle/used yourself) — also worth checking your local library or an
O'Reilly/Safari subscription if you have one before buying new.

- [00-foundations.md](00-foundations.md)
- [01-market-mechanics.md](01-market-mechanics.md)
- [02-programming-data-pipeline.md](02-programming-data-pipeline.md)
- [03-strategy-families.md](03-strategy-families.md)
- [04-build-the-system.md](04-build-the-system.md) — the final project
- [05-statistical-validation.md](05-statistical-validation.md)
- [06-risk-position-sizing.md](06-risk-position-sizing.md)
- [07-paper-trading-live.md](07-paper-trading-live.md)
- [08-infrastructure.md](08-infrastructure.md)
- [09-specialize.md](09-specialize.md)
- [signals.md](signals.md) — how you'll know it's actually working

## Two directions this field can go

- **Practitioner (this guide)** — build, validate, and run your own systems
  against your own capital. Self-taught, engineering-led, no gatekeeper.
  Realistic given a backend background and no finance degree.
- **Quant researcher/developer** — hired into a fund or prop shop. Usually
  wants a strong stochastic-calculus/stats background or a
  competitive-programming pedigree for HFT roles. Not the focus here, though
  Phases 00–05 overlap almost entirely with that path too.

## Timeline (part-time, ~5–10 hrs/week)

| Phases | What | Duration |
|---|---|---|
| 00–03 | Foundations, market mechanics, data pipeline, strategy theory | 5–7 weeks |
| 04 | Build the mean-reversion system (the core coding project) | 3–4 weeks |
| 05 | Statistical validation (walk-forward, Monte Carlo) | 2–3 weeks |
| 06 | Risk & position sizing | 1 week |
| 07 | Paper trading | **3 months minimum, wall-clock** — this can't be rushed, the market has to generate enough sample trades |
| 08 | Infrastructure (runs in parallel with 07) | 1–2 weeks |
| 09 | Specialize | open-ended, ongoing |

**Realistic total: 6–9 months** part-time to go from zero to a running,
validated system with a real paper-trading track record. That gets you a
working system, not mastery — expect another 1–2 years of iteration (adding
strategies, surviving a drawdown, refining risk rules) before "professional"
stops feeling like a stretch. Phase 05 is the phase most self-taught traders
skip, and skipping it is the single biggest reason systems that look great in
backtest lose money live.
