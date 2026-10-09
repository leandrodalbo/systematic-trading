https://docs.alpaca.markets/us/docs/getting-started



- paper trading endpoint

https://paper-api.alpaca.markets/v2

- env variables

APCA_API_KEY_ID=...
APCA_API_SECRET_KEY=...


- quick api test

curl -H "APCA-API-KEY-ID: $APCA_API_KEY_ID" -H "APCA-API-SECRET-KEY: $APCA_API_SECRET_KEY" \
  https://paper-api.alpaca.markets/v2/account
