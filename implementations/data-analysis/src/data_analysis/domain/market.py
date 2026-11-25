from dataclasses import dataclass
from enum import Enum

import pandas as pd


class AssetClass(Enum):
    STOCK = "stocks"
    CRYPTO = "crypto"


class MarketDirection(Enum):
    BULLISH = "bullish"
    BEARISH = "bearish"


class Since(Enum):
    CLOSE = "close"
    HIGH = "high"
    LOW = "low"


class Cross(Enum):
    GOLDEN = "golden"
    DEATH = "death"


@dataclass(frozen=True)
class LastCross:
    date: pd.Timestamp
    kind: Cross
