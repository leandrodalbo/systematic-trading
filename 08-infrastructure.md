# Phase 08 — Infrastructure

**Goal:** make the system run unattended and tell you when it breaks. This
is where your existing backend/AWS/Terraform skills do the heavy lifting —
can run in parallel with Phase 07.

## Learn

- Scheduled execution (cron / a small Spring Boot job) instead of running
  scripts by hand
- Logging and alerting when a data feed drops or an order fails
- Keeping strategy code, backtest code, and live execution code from
  silently drifting apart

## Free tools

- Your own AWS/Terraform experience — a small EC2 box or Lambda, Postgres
  for trade/data storage, CloudWatch-style alerting

## Steps

1. Move the Phase 07 paper-trading app from "run by hand" to a scheduled
   job (Spring `@Scheduled`, cron, or similar). Deploy the Python signal
   service next to it.
2. Add logging for every run: data fetched, signals generated, orders
   placed, errors.
3. Add alerting for failure states (data feed down, order rejected,
   unhandled exception) — even a simple email/Slack webhook is enough.
4. Set up a single source of truth for strategy parameters so backtest and
   live code can't silently diverge (e.g. a shared config file, not two
   copies of the logic).

## Milestone

- [ ] The system runs on a schedule with no manual intervention and pages
      you on failure.

**Next:** [09-specialize.md](09-specialize.md)
