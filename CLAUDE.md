# Claude Code Instructions: Systematic Trading

## Development method: TDCG

Every feature or behavior change follows this loop. Do not skip steps. Ask before moving to the next one.

```
1. SPECIFY  — agree on what to build, in plain language
2. RED      — generate failing tests; wait for human approval before continuing
3. PROMPT   — generate the implementation using the approved tests as the spec
4. GREEN    — tests pass; do not weaken assertions to force it
5. REVIEW   — check naming, single responsibility, duplication, dead code
6. REFACTOR — apply what review found; tests stay green throughout
7. COMMIT   — one small focused commit with a clear why
```

### Rules

- Always ask before moving to the next step. Never chain steps.
- Generate tests first. Wait for approval. Then generate the implementation.
- If a test fails, fix the implementation or the spec. Never change the test to make it pass.
- One behavior per cycle. If the prompt grows, split the cycle.
- Docs and notes changes do not need a full cycle. Confirm with the user and apply.
- Always check existing similar code before generating anything.

## Project

Self-directed curriculum from statistics to a validated mean-reversion swing-trading system, for US stocks and crypto. Free tools only.

- `0X-*.md` — one file per phase (00–09). Progress is tracked by the milestone checkbox and a `Status` line.
- `foundations/` — notes and cheat sheets. Architecture: `foundations/architecture.md`.
- `implementations/data-analysis` — Python: the strategy (indicators, signals, backtest, stats). Becomes a FastAPI signal service in Phase 07.
- `implementations/trade-plumbing` — Kotlin + Spring Boot: fetch/store data, call the Python service, place orders, scheduling, alerts.
- `data/` — one CSV per ticker in `data/stocks/` and `data/crypto/`.
- Broker: Alpaca (paper first) for stocks and crypto. Kraken is parked for later.

Golden rule: the Python code we backtest is the same code that makes live signals. Kotlin never decides a trade.

Roadmap: `README.md`
Social posts (LinkedIn/X): follow `posts/GUIDE.md`; drafts go in `posts/`.
Method reference: `TDCG/README.md`

## Writing notes

- Plain, friendly English. Short. Readable at a glance.
- Short tables are fine for quick comparisons. No long text inside table cells.
- The user writes the learning exercises; help and review, don't do them unless asked.
