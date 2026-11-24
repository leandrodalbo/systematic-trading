#!/usr/bin/env bash
# Fetch the latest daily bars for every ticker in the watch list.
# trade-plumber must be running. Run by hand before the report.
set -euo pipefail

WATCHLIST="$(dirname "$0")/../data-analysis/watchlist.csv"
BATCH_URL="http://localhost:8080/market-data/updates/batch"
TIMEFRAME="DAY"

body=$(tail -n +2 "$WATCHLIST" | jq -R -s --arg timeframe "$TIMEFRAME" '
  {updates: split("\n") | map(select(length > 0) | split(",")
    | {symbol: .[0], assetClass: .[1], timeframe: $timeframe})}')

curl -sS --fail-with-body -X POST "$BATCH_URL" \
  -H 'Content-Type: application/json' \
  -d "$body"
echo "done: $(tail -n +2 "$WATCHLIST" | grep -c .) tickers updated"
