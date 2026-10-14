# Alpaca + Kraken — API cheat sheet

**Current setup: Alpaca only, for US stocks and crypto.** Kraken waits until we have more Alpaca experience.

## Alpaca crypto (what we use now)

- Same account, keys and paper account as stocks. Symbols like `BTC/USD`.
- 20+ coins, 56 pairs (USD, USDT, USDC, BTC quotes). 24/7. All coins fractional.
- Orders: market and limit (`gtc`, `ioc`), stop_limit (`gtc` only). Max $200k per order. No shorting, no leverage.
- Fees: 0.15% maker / 0.25% taker at low volume, charged in the asset received, calculated end of day.
- Data: `GET /v1beta3/crypto/us/bars?symbols=BTC/USD&timeframe=1Day`, up to 10,000 bars/page, paginate with `next_page_token`. Bars come from **quote midpoints**, so a bar can show a price with zero volume.
- **Check how far back history goes**: the docs give no start date. Phase 04 wants 5+ years; if short, bring in Kraken's CSV history.

## Accounts & keys

- **Alpaca:** UK residents accepted (UTR or NI number). Funding **USD only** (via Rapyd), no ISA. Paper and live have **different keys and base URLs**.
  - Live `https://api.alpaca.markets` · Paper `https://paper-api.alpaca.markets` · Market data (both) `https://data.alpaca.markets`
  - Auth headers: `APCA-API-KEY-ID` + `APCA-API-SECRET-KEY`
- **Kraken:** FCA-registered, GBP deposits. One key per purpose, **never enable "Withdraw Funds"** on a trading key, IP-whitelist it.
  - REST: `https://api.kraken.com/0/public/...` (no key), `/0/private/...` (signed).
  - Auth headers `API-Key` + `API-Sign`, where `API-Sign = base64(HMAC-SHA512(base64decode(secret), path + SHA256(nonce + postdata)))`.
  - **Nonce** must always increase per key (ms timestamp). Sharing a key across two processes risks out-of-order nonces → rejected.
  - Permissions needed: Query Funds, Query Open Orders & Trades, Create & Modify Orders, Cancel/Close Orders, Access WebSocket API.

## Market data (Phase 02)

| | Alpaca (stocks) | Kraken (crypto, later) |
|---|---|---|
| Daily bars | `GET /v2/stocks/bars?timeframe=1Day` | `GET /0/public/OHLC?interval=1440` |
| History | since 2016 | **latest 720 candles only** (`since` can't go further) |
| Page size | 10,000, `next_page_token` | 720, no paging |
| Fields | t, o, h, l, c, v, n (trades), vw (VWAP) | time, o, h, l, c, vwap, volume, count |
| Adjustments | `adjustment=raw\|split\|dividend\|all` | n/a |
| Rate limit | 200 req/min (free) | counter max 15, decays 0.33/s (Starter) |

Gotchas:
- **Kraken 720 cap:** 2 years daily = 730 > 720. For long history use Kraken's **downloadable OHLCVT CSVs** (full history to 30 Jun 2026, quarterly updates, ~2 GB parts), then top up from OHLC. No header row; empty intervals are omitted, not zero-filled.
- **Alpaca free plan = IEX real-time**, but historical **SIP** (all exchanges) is allowed except the latest 15 min. Pass `feed=sip` for daily history; IEX-only bars are a small share of volume.
- **Use `adjustment=all`** for backtests so splits/dividends don't create fake jumps.
- **Kraken pair names** use X/Z prefixes and XBT (`XXBTZUSD`); `assetVersion=1` gives `BTC/USD`. Normalise symbols in one place.
- Never mix Kraken and Alpaca crypto bars in one series (midpoints vs. trades).

Real-time (later):
- Alpaca: websocket for stocks (free: 30 symbols, IEX); `trade_updates` stream for your fills.
- Kraken WebSocket v2: public `wss://ws.kraken.com/v2` (ticker, book, ohlc, trade), private `wss://ws-auth.kraken.com/v2` (executions, balances, `add_order`, `cancel_order`). Private needs a 15-min token from `POST /0/private/GetWebSocketsToken`. Idle closes after ~1 min, so ping. Reconnect limit ~150 per 10 min, else 10-min IP ban.

## Orders

| | Alpaca (stocks) | Kraken (spot, later) |
|---|---|---|
| Types | market, limit, stop, stop_limit, trailing_stop | market, limit, stop-loss, take-profit, stop-loss-limit, take-profit-limit, trailing-stop(-limit), iceberg |
| Combos | bracket, OCO, OTO | conditional close (`close[ordertype]`, `close[price]`) |
| Time in force | day, gtc (auto-cancel 90 days), opg, cls, ioc\*, fok\* | GTC (default), IOC, GTD, FOK |
| Post-only | no | `oflags=post` |
| Dry run | paper account | `validate=true` (checks, doesn't place) |
| Own order ID | `client_order_id` | `cl_ord_id` or `userref` |

\*ioc/fok/opg/cls on whole-share stock orders need approval from Alpaca sales.

Gotchas:
- **Alpaca fractional/notional:** DAY only; notional orders can't be replaced (cancel + resubmit).
- **Alpaca extended hours:** limit only, `day`/`gtc`, no brackets.
- **Alpaca price increments:** 2 decimals ≥ $1, 4 decimals < $1, else rejected.
- **Kraken sizing:** read `ordermin`, `costmin`, `tick_size`, `lot_decimals`, `pair_decimals` from `GET /0/public/AssetPairs` and round every order. Check `status`: a pair can be `cancel_only` / `post_only`.
- **Kraken `fees`/`fees_maker` in AssetPairs are deprecated** (empty since 8 Sep 2026): take the rate from the fee schedule / your tier.
- Always check order **status** (Alpaca: `new`, `partially_filled`, `filled`, `canceled`, `expired`, `rejected`, …). Submitted ≠ filled.

## Fees (what backtests forget)

- **Alpaca stocks:** $0 commission (retail). Still charged end of day: FINRA **TAF on sells**, **CAT on buys and sells**. Rates: [alpaca.markets/disclosures](https://alpaca.markets/disclosures). Plus USD↔GBP conversion when funding.
- **Kraken Pro spot (since 9 Jul 2026), entry tier** (< $2.5K 30-day volume): **0.40% maker / 0.80% taker**. Tier = best of spot volume, futures volume, or assets held. A market-order round trip ≈ 1.6% before slippage, which kills most short-term crypto strategies at small size. Prefer **limit / post-only** and low turnover.

## Rate limits

- **Alpaca trading:** 200 req/min per account → HTTP 429. Raising it means a non-retail account with per-share fees. Don't.
- **Kraken REST:** per-key counter (+1 per call, +2 ledger/trade history), Starter max 15, decays 0.33/s → `EAPI:Rate limit exceeded`.
- **Kraken trading:** separate **per-pair** counter, Starter threshold 60, decays 1/s. Add order +1; **cancel within 5 s costs +8**, so place/cancel loops get throttled. Max 60 open orders per pair (Starter).

## Paper trading (Phase 07)

- **Alpaca paper:** $100k default, real-time prices. Does **not** simulate slippage, market impact, queue position, price improvement, regulatory fees, dividends. Partial fills random (~10%); fills orders bigger than real liquidity. → Paper is an optimistic upper bound.
- **Kraken has no spot sandbox.** Options:
  1. **Kraken CLI** (`kraken paper buy BTCUSD 0.01`): local engine on the live ticker, no keys. Defaults: fee **0.26%** (old Starter rate), slippage **0**. Override both or results are too rosy.
  2. Your own paper-fill simulator on Kraken's public WebSocket with your real fee tier + a slippage model. More work, but it's the same code path backtesting needs.
  - Kraken futures demo is useless: UK retail can't trade crypto derivatives (FCA ban).

## Market structure differences

- **Hours:** US stocks 9:30–16:00 ET (+ pre/after/overnight, limit-only); use Alpaca `/v2/clock` and `/v2/calendar` for holidays/half-days. Crypto 24/7, but thin at weekends and overnight.
- **PDT rule:** retired by FINRA from 4 Jun 2026, no $25k minimum on Alpaca. Never applied to crypto.
- **Currency:** Alpaca USD, Kraken GBP or USD pairs. Pick one reporting currency for P&L.
- **Shorting/leverage:** stocks can be shorted on an Alpaca margin account; Kraken crypto is effectively long-only for UK retail.

## SDKs

- Alpaca: official Python (`alpaca-py`), Go, NodeJS, C#. No Java SDK, so we call REST from Kotlin ([architecture.md](architecture.md)).
- Kraken: simple enough to call directly; `ccxt` (Python) wraps it if you'd rather not write the signing. Kraken CLI for paper.

## Sources

- [Alpaca orders](https://docs.alpaca.markets/docs/orders-at-alpaca) · [paper trading](https://docs.alpaca.markets/docs/paper-trading) · [market data plans](https://docs.alpaca.markets/docs/about-market-data-api) · [stock bars](https://docs.alpaca.markets/us/reference/stockbars) · [crypto data](https://docs.alpaca.markets/us/docs/historical-crypto-data-1) · [regulatory fees](https://docs.alpaca.markets/us/docs/regulatory-fees) · [rate limit](https://alpaca.markets/support/usage-limit-api-calls) · [PDT retired](https://alpaca.markets/blog/finra-retires-the-pdt-rule-introducing-alpacas-new-intraday-margin-framework/) · [non-US accounts](https://alpaca.markets/learn/live-trading-account-non-us)
- [Kraken REST auth](https://docs.kraken.com/exchange/guides/rest/authentication) · [REST rate limits](https://docs.kraken.com/exchange/guides/rest/ratelimits) · [trading limits](https://docs.kraken.com/exchange/guides/general/ratelimits) · [AddOrder](https://docs.kraken.com/api/docs/rest-api/add-order) · [OHLC](https://docs.kraken.com/api/docs/rest-api/get-ohlc-data) · [AssetPairs](https://docs.kraken.com/api/docs/rest-api/get-tradable-asset-pairs) · [WebSocket v2](https://docs.kraken.com/api/docs/guides/spot-ws-intro) · [historical CSVs](https://support.kraken.com/articles/360047124832-downloadable-historical-ohlcvt-open-high-low-close-volume-trades-data) · [fee schedule](https://www.kraken.com/features/fee-schedule) · [Kraken CLI](https://github.com/krakenfx/kraken-cli)
