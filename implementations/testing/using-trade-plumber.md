- before you start: `TRADE_PLUMBER_DATA_DIR` must be set (it is, in `~/.zshrc`). In IntelliJ, add it to the run config.

- fetching data

curl -i -X POST localhost:8080/market-data/updates -H 'Content-Type: application/json' \
  -d '{"symbol":"AAPL","assetClass":"STOCK","timeframe":"DAY"}'

curl -i -X POST localhost:8080/market-data/updates -H 'Content-Type: application/json' \
  -d '{"symbol":"BTC/USD","assetClass":"CRYPTO","timeframe":"DAY"}'

- checking the files from Python

cd implementations/data-analysis
uv run python scripts/load_bars_check.py
