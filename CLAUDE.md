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
- Config-only changes (e.g. `application.yml`) do not need a new test. Make the change and keep the existing tests green.
- Always check existing similar code before generating anything.
- The user makes all commits. At COMMIT, suggest the files and a message; never run `git commit` or `git push`.

## Project

Self-directed curriculum from statistics to a validated mean-reversion swing-trading system, for US stocks and crypto. Free tools only.

- `0X-*.md` — one file per phase (00–09). Progress is tracked by the milestone checkbox and a `Status` line.
- `foundations/` — notes and cheat sheets. Architecture: `foundations/architecture.md`.
- `implementations/data-analysis` — Python: the strategy (indicators, signals, backtest, stats). Becomes a FastAPI signal service in Phase 07.
- `implementations/trade-plumber` — Kotlin + Spring Boot: fetch/store data, call the Python service, place orders, scheduling, alerts.
- Price data: trade-plumber writes one CSV per ticker under `~/.local/share/trade-plumber` (`storage.baseDir`). There is no `data/` folder in the repo.
- Data folder setting: `TRADE_PLUMBER_DATA_DIR` is the only variable for it, in Kotlin and Python alike. No fallbacks (no `XDG_DATA_HOME`, no defaults): if it's missing, fail with a clear error. Set in `~/.zshrc`.
- `streaming/` (git-ignored) — YouTube channel: OBS setup, video scripts, assets. `ytb-key` is secret.
- Broker: Alpaca (paper first) for stocks and crypto. Kraken is parked for later.

Golden rule: the Python code we backtest is the same code that makes live signals. Kotlin never decides a trade.

Roadmap: `README.md`
Social posts (LinkedIn/X): follow `../POSTS-GUIDE.md`.
Method reference: `TDCG/README.md`

## Code style (mandatory)

- Kotlin: define constant values (query params, paths, headers, fixed values) as `const val` in a `companion object`. No inline string literals for them.
- Kotlin tests: use MockK (`mockk`, `every`, `verify`) for test doubles. No hand-written fakes.
- Kotlin: use `runCatching { }.onFailure { }` instead of try/catch. It catches every `Throwable`, so always rethrow (`throw it`) unless swallowing is truly intended.
- Python layout: folders by area. `core/` = shared pieces (`config`, `constants`, `errors`), `domain/` = types, `data/` = reading price data. Later: `indicators/`, `backtest/`, `api/` (FastAPI, Phase 07). Dependencies point inward only: `api` → others → `data` → `domain`/`core`. No folder for a single file.
- Python: all error classes live in `core/errors.py`. No empty classes (`pass`): each error has an `__init__` that builds its own message from its arguments. No enum of messages.
- Python: env var names and the functions that read them live in `core/config.py`.
- Python: fixed values (column names, timezones, separators, file extensions, defaults) live in `core/constants.py`. No inline literals for them, not even small pieces inside f-strings (e.g. `_`, `.csv`).
- Python: domain types (enums, data classes) live in `domain/`, one module per type (e.g. `domain/asset_class.py`). Enum names match the Kotlin ones (e.g. `AssetClass.STOCK`, `CRYPTO`), since the two services talk to each other.
- Python: helpers used only inside a module start with `_` (private by convention). Only the module's real entry points are public.
- Python: keep functions short and single-purpose; pull a step out into a private helper when a function does two jobs.
- Python: one job per module. One-off checks go in `scripts/`, never in the package.
- Python tests: pytest, one `test_<module>.py` per module, mirroring the package folders (`tests/core/`, `tests/data/`).

## Writing notes

- Plain, friendly English. Short. Readable at a glance.
- Short tables are fine for quick comparisons. No long text inside table cells.
- The user writes the learning exercises; help and review, don't do them unless asked.
