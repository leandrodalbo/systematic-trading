# Phase 08 — Infrastructure

**Goal:** the system runs unattended and tells you when it breaks. Your backend/AWS/Terraform skills do the heavy lifting here. Runs in parallel with Phase 07.

## Learn

- Scheduled runs (cron / Spring Boot job) instead of manual scripts
- Logging and alerts when a data feed drops or an order fails
- Keeping strategy, backtest and live code from silently drifting apart

## Free tools

- Your AWS/Terraform experience: a small EC2 box or Lambda, Postgres for trades/data, CloudWatch-style alerts

## Steps

1. Move the Phase 07 app to a scheduled job (Spring `@Scheduled`, cron, …). Deploy the Python signal service next to it.
2. Log every run: data fetched, signals, orders, errors.
3. Alert on failures (feed down, order rejected, unhandled exception). An email/Slack webhook is enough.
4. One source of truth for strategy parameters (e.g. a shared config file), so backtest and live can't diverge.

## Milestone

- [ ] The system runs on a schedule with no manual intervention and pages
      you on failure.

**Next:** [09-specialize.md](09-specialize.md)
