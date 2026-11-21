---
title: One system, two languages
description: "Kotlin for the plumbing, Python for the research. Each language picked as the right tool for its job."
date: 2026-11-21T18:00:00Z
---

## Two jobs

A trading system has two very different jobs:

- **Plumbing:** fetch prices, store them, run things on a schedule, and later send orders to the
  broker and check risk limits before each one.
- **Research:** look at the data, build indicators, test ideas on years of history, and decide
  what to trade today.

## The right tool for each job

- **Kotlin + Spring Boot for the plumbing (trade-plumber).** This is backend work: HTTP calls to
  the broker (Alpaca), files, scheduling, clear errors, tests. It's what I do every day, and
  Spring Boot gives all of it out of the box.
- **Python for the research (data-analysis).** pandas and the Python data tools are where
  trading research lives. Loading years of prices and testing an idea takes a few lines.

## One rule: the strategy lives only in Python

The Python code that runs the backtest is the **same code** that will make the live signals.
Kotlin never decides a trade: it carries out what Python says.

Two copies of a strategy, one in each language, would slowly drift apart. Then the backtest
would be testing something the live system doesn't do.

## How they talk

- **Through the same files.** trade-plumber writes one CSV file per ticker (time, open, high,
  low, close, volume). Python reads the same files. No prices go over the network between them.
- **One setting for the data folder**, used by both sides. If it's missing, both fail with a
  clear error, never a guessed default.
- Later, Python becomes a small service: trade-plumber asks "signals for today?" and gets back a
  short list of orders.
