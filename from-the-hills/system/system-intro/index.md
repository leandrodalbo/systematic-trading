---
title: One system, two languages
description: "Kotlin for the plumbing, Python for the research. Each language picked as the right tool for its job."
date: 2026-11-21T18:00:00Z
---

## Two jobs, two languages

- **Kotlin + Spring Boot (trade-plumber):** the plumbing. Fetch prices and store them. Later:
  send orders to the broker (Alpaca). Spring Boot is mature for this: HTTP
  calls, scheduling, error handling and tests are all built in.
- **Python (data-analysis):** the research. Test ideas on years of past prices and decide what
  to trade. Python is mature for this: pandas, NumPy and most trading research tools.

## One rule: only Python decides trades

The Python part that tests an idea on past prices is the same one that picks the real trades. Kotlin
just carries them out. Two copies would drift apart, and the tests would check the wrong thing.
