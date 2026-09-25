# Alpaca + Kraken — API cheat sheet

**Current setup: Alpaca only, for both US stocks and crypto.** 

Not using Kraken until we have more experience with Alpaca.


## Alpaca crypto (what we use now)

- Same account, keys and paper account as stocks. Symbols are `BTC/USD` style.
- 20+ coins, 56 pairs (USD, USDT, USDC, BTC quotes). 24/7. Every coin can be bought in fractions.

- Orders: market and limit (`gtc`, `ioc`), stop_limit (`gtc` only). Max $200k per order. No shorting, no leverage.

- Fees: 0.15% maker / 0.25% taker at low volume, charged in the asset you receive, calculated end-of-day.

- Data: `GET /v1beta3/crypto/us/bars?symbols=BTC/USD&timeframe=1Day`, up to 10,000 bars per page, paginate with `next_page_token`. Bars are built from  **quote midpoints**, so a bar can show a price with zero volume.

- **Check how far back crypto history goes** before relying on it — the docs don't state a start date. Phase 04 wants 5+ years; if Alpaca falls short, that's the moment to bring in Kraken's CSV history.

## Accounts & keys

- **Alpaca** — UK residents accepted (UTR or NI number). Funding in **USD only** (via Rapyd), no ISA. Paper and live use **different keys and base URLs**.
  
  - Live trading: `https://api.alpaca.markets`
  - Paper trading: `https://paper-api.alpaca.markets`
  - Market data (both): `https://data.alpaca.markets`
  - Auth: headers `APCA-API-KEY-ID` + `APCA-API-SECRET-KEY`.

- **Kraken** — FCA-registered in the UK, GBP deposits. One key per purpose;  **never enable "Withdraw Funds"** on a trading key; 

IP-whitelist it.
  - REST: `https://api.kraken.com/0/public/...` (no key) and
    `/0/private/...` (signed).
  - Auth: headers `API-Key` + `API-Sign`, where
    `API-Sign = base64(HMAC-SHA512(base64decode(secret), path + SHA256(nonce + postdata)))`.
  - **Nonce** must always increase per key (ms timestamp). Never reuse a key across two processes without care — out-of-order nonces get rejected.
  - Key permissions needed: Query Funds, Query Open Orders & Trades, Create &  Modify Orders, Cancel/Close Orders, Access WebSocket API.

## Market data (what Phase 02 needs)

**Alpaca (stocks):**

- Daily bars: `GET /v2/stocks/bars?timeframe=1Day`
- History: since 2016
- Page size: up to 10,000 bars, paginate with `next_page_token`
- Bar fields: t, o, h, l, c, v, n (trades), vw (VWAP)
- Adjustments: `adjustment=raw|split|dividend|all`
- Rate limit: 200 req/min (free plan)

**Kraken (crypto, later):**

- Daily bars: `GET /0/public/OHLC?interval=1440`
- History: **only the latest 720 candles** — `since` can't go further back
- Page size: 720, no paging
- Bar fields: time, o, h, l, c, vwap, volume, count
- Adjustments: n/a
- Rate limit: counter max 15, decays 0.33/s (Starter tier)

Gotchas:

- **Kraken 720-candle cap:** 2 years of daily bars = 730 > 720. For long history, use Kraken's **downloadable OHLCVT CSV files** (full history up to 30 June 2026, quarterly updates, split into ~2 GB parts), then top up from the OHLC endpoint. CSVs have no header row, and gaps (intervals with no trades) are left out rather than filled with zeros.

- **Alpaca free plan = IEX real-time feed**, but historical **SIP**
  (all exchanges) is allowed as long as you don't ask for the most recent 15 minutes. For daily history, pass `feed=sip` — IEX-only bars reflect a small share of volume.
- **Use `adjustment=all`** for backtests so splits/dividends don't create fake jumps.

- **Kraken pair naming:** internal names use X/Z prefixes and XBT
  (`XXBTZUSD`); pass `assetVersion=1` to get `BTC/USD`. Normalise symbols in one place.
- When Kraken is added, don't mix its bars with Alpaca's crypto bars in one series — Alpaca's are built from quote midpoints, not Kraken trades.

Real-time (later phases):

- Alpaca: websocket streaming for stocks (free plan: 30 symbols, IEX);
  `trade_updates` stream for your own order fills.
- Kraken WebSocket v2: public `wss://ws.kraken.com/v2` (ticker, book, ohlc, trade), private `wss://ws-auth.kraken.com/v2` (executions, balances, `add_order`, `cancel_order`). Private needs a 15-min token from `POST /0/private/GetWebSocketsToken`. Idle connections close after ~1 min — ping. Reconnect limit ~150 per 10 min or a 10-min IP ban.

## Orders

**Alpaca (stocks):**

- Types: market, limit, stop, stop_limit, trailing_stop
- Combos: bracket, OCO, OTO
- Time in force: day, gtc (auto-cancels after 90 days), opg, cls, ioc\*,
  fok\*
- Post-only: not available
- Dry run: use the paper account
- Your own order ID: `client_order_id`

**Kraken (spot, later):**

- Types: market, limit, stop-loss, take-profit, stop-loss-limit,
  take-profit-limit, trailing-stop(-limit), iceberg
- Combos: conditional close (`close[ordertype]`, `close[price]`)
- Time in force: GTC (default), IOC, GTD, FOK
- Post-only: `oflags=post`
- Dry run: `validate=true` — checks the order, doesn't place it
- Your own order ID: `cl_ord_id` or `userref`

*ioc/fok/opg/cls on whole-share stock orders need approval from Alpaca's sales team.

Gotchas:

- **Alpaca fractional/notional orders:** DAY only, and notional orders
  can't be replaced (cancel + resubmit).

- **Alpaca extended hours:** limit orders only, `day`/`gtc`, no brackets.
- **Alpaca price increments:** 2 decimals ≥ $1, 4 decimals < $1 —
  otherwise rejected.
- **Kraken sizing:** read `ordermin`, `costmin`, `tick_size`,
  `lot_decimals`, `pair_decimals` from `GET /0/public/AssetPairs` and round
  every order to them. Check `status` — a pair can be `cancel_only` /
  `post_only`.
- **Kraken `fees`/`fees_maker` in AssetPairs are deprecated** (empty since
  8 Sep 2026) — take your fee rate from the fee schedule / your account tier.
- Always check order **status** (Alpaca: `new`, `partially_filled`,
  `filled`, `canceled`, `expired`, `rejected`, …) — a submitted order is
  not a filled order.

## Fees (the thing backtests forget)

- **Alpaca stocks:** $0 commission (retail, self-directed). Regulatory fees
  are still charged: FINRA **TAF on sells**, **CAT on buys and sells**,
  deducted end-of-day. Current rates:
  [alpaca.markets/disclosures](https://alpaca.markets/disclosures). Plus the
  USD↔GBP conversion cost when funding.
- **Kraken Pro spot (since 9 Jul 2026), entry tier** (< $2.5K 30-day
  volume): **0.40% maker / 0.80% taker**. Tier is the best of spot volume,
  futures volume, or assets held on platform.
  → A market-order round trip costs ~1.6% before slippage. That
  kills most short-term crypto strategies at small size — prefer **limit /
  post-only** orders and low turnover.

## Rate limits

- **Alpaca trading API:** 200 req/min per account → HTTP 429. Raising it
  means a non-retail account (and per-share fees) — don't.
- **Kraken REST:** per-key call counter (+1 per call, +2 for ledger/trade
  history). Starter max 15, decays 0.33/s → `EAPI:Rate limit exceeded`.
- **Kraken trading:** separate **per-pair** counter. Starter threshold 60,
  decays 1/s. Adding an order +1; **cancelling within 5 s of placing costs
  +8** — rapid place/cancel loops get throttled. Max 60 open orders per
  pair (Starter).

## Paper trading (Phase 07)

- **Alpaca paper:** $100k default, real-time prices. Does **not** simulate:
  slippage, market impact, queue position, price improvement, regulatory
  fees, dividends. Partial fills random (~10%); fills orders bigger than real
  liquidity. → Paper results are an optimistic upper bound.
- **Kraken has no spot sandbox.** Options:
  1. **Kraken CLI** (`kraken paper buy BTCUSD 0.01`) — local engine against
     the live ticker, no keys. Default fee is **0.26%** (the old Starter rate)
     and slippage **0** — override both to your real tier or results will be
     too rosy.
  2. Your own paper-fill simulator fed by Kraken's public WebSocket, with
     your real fee tier + a slippage model. More work, but it's the
     same code path you'll need for backtesting.
  - Kraken's futures demo isn't useful: UK retail can't trade crypto
    derivatives (FCA ban).

## Market structure differences (for the step-3 comparison)

- **Hours:** US stocks 9:30–16:00 ET regular (+ pre/after/overnight
  sessions, limit-only); use Alpaca's `/v2/clock` and `/v2/calendar` for
  holidays/half-days. Crypto 24/7 — but liquidity thins at weekends and
  overnight.
- **PDT rule:** retired by FINRA effective 4 June 2026 — no $25k minimum
  for day trading on Alpaca any more. Never applied to crypto.
- **Currency:** Alpaca = USD, Kraken = GBP or USD pairs. Pick one
  reporting currency for P&L and convert consistently.
- **Shorting/leverage:** stocks can be shorted on a margin account at
  Alpaca; crypto on Kraken is effectively long-only for a UK retail account.

## SDKs

- Alpaca: official Python (`alpaca-py`), Go, NodeJS, C#. No official Java
  SDK — we call the REST API directly from Kotlin (see
  [architecture.md](architecture.md)).
  
- Kraken: REST/WebSocket are simple enough to call directly; `ccxt` (Python)
  wraps Kraken if you'd rather not write the signing yourself. Kraken CLI for
  paper trading.

## Sources

- [Alpaca orders](https://docs.alpaca.markets/docs/orders-at-alpaca) ·
  [paper trading](https://docs.alpaca.markets/docs/paper-trading) ·
  [market data plans](https://docs.alpaca.markets/docs/about-market-data-api) ·
  [stock bars](https://docs.alpaca.markets/us/reference/stockbars) ·
  [crypto data](https://docs.alpaca.markets/us/docs/historical-crypto-data-1) ·
  [regulatory fees](https://docs.alpaca.markets/us/docs/regulatory-fees) ·
  [rate limit](https://alpaca.markets/support/usage-limit-api-calls) ·
  [PDT retired](https://alpaca.markets/blog/finra-retires-the-pdt-rule-introducing-alpacas-new-intraday-margin-framework/) ·
  [non-US accounts](https://alpaca.markets/learn/live-trading-account-non-us)
- [Kraken REST auth](https://docs.kraken.com/exchange/guides/rest/authentication) ·
  [REST rate limits](https://docs.kraken.com/exchange/guides/rest/ratelimits) ·
  [trading limits](https://docs.kraken.com/exchange/guides/general/ratelimits) ·
  [AddOrder](https://docs.kraken.com/api/docs/rest-api/add-order) ·
  [OHLC](https://docs.kraken.com/api/docs/rest-api/get-ohlc-data) ·
  [AssetPairs](https://docs.kraken.com/api/docs/rest-api/get-tradable-asset-pairs) ·
  [WebSocket v2](https://docs.kraken.com/api/docs/guides/spot-ws-intro) ·
  [historical CSVs](https://support.kraken.com/articles/360047124832-downloadable-historical-ohlcvt-open-high-low-close-volume-trades-data) ·
  [fee schedule](https://www.kraken.com/features/fee-schedule) ·
  [Kraken CLI](https://github.com/krakenfx/kraken-cli)
