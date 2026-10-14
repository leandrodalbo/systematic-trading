- fetching data

curl -i -X POST localhost:8080/market-data/updates -H 'Content-Type: application/json' \
  -d '{"symbol":"AAPL","assetClass":"STOCK","timeframe":"DAY"}'

curl -i -X POST localhost:8080/market-data/updates -H 'Content-Type: application/json' \
  -d '{"symbol":"BTC/USD","assetClass":"CRYPTO","timeframe":"DAY"}'
