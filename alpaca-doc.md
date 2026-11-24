# Alpaca

- Docs: https://docs.alpaca.markets/us/docs/getting-started
- Prices (what trade-plumber uses): `https://data.alpaca.markets`
- Paper trading: `https://paper-api.alpaca.markets/v2`
- Keys, set in `~/.zshrc` (never in the repo): `APCA_API_KEY_ID`, `APCA_API_SECRET_KEY`

Quick check that the keys work:

```bash
curl -H "APCA-API-KEY-ID: $APCA_API_KEY_ID" -H "APCA-API-SECRET-KEY: $APCA_API_SECRET_KEY" \
  https://paper-api.alpaca.markets/v2/account
```
