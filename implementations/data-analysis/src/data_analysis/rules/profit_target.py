from data_analysis.core.constants import PERCENT


def profit_target_reached(entry_close: float, close: float, target_percent: float) -> bool:
    """A `target_percent` of 5 means the close is at least 5% above the entry close."""
    return close >= entry_close * (1 + target_percent / PERCENT)
