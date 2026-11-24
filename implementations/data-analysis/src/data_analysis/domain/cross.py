from dataclasses import dataclass
from enum import Enum

import pandas as pd


class Cross(Enum):
    GOLDEN = "golden"
    DEATH = "death"


@dataclass(frozen=True)
class LastCross:
    date: pd.Timestamp
    kind: Cross
