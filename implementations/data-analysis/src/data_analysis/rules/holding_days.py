def holding_days_reached(days_held: int, holding_days: int) -> bool:
    """`days_held` counts bars since the entry bar, which is day 0."""
    return days_held >= holding_days
