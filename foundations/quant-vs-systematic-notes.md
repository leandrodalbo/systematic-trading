# Quantitative vs Systematic Trading

They overlap a lot, and people often use the two words for the same thing. But they answer different questions.

## Systematic: how decisions are made

- Fixed rules decide every entry, exit and position size.
- The system runs them the same way every time. No person makes the call.

## Quantitative: how the edge is found and checked

- Uses maths, statistics and data to find patterns.
- Tests whether a pattern is real or just luck.

## You can have one without the other

| | Example |
|---|---|
| Systematic, not very quant | "Buy when price breaks the 20-day high", run mechanically, with little testing behind it |
| Quant, not systematic | Statistical models for research, but a person still decides each trade |
| Both | Bandy's approach, and this project |

## In the industry

- **Systematic** is often used for trend-following funds (CTAs).
- **Quant** is often used for statistics-heavy funds (stat-arb, high-frequency trading).

## In this project

- **Quant:** the Python side (`data-analysis`) finds and tests the edge.
- **Systematic:** the same rules run automatically through `trade-plumber`.
