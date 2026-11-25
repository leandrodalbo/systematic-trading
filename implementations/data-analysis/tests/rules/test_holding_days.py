from data_analysis.rules.holding_days import holding_days_reached


def test_holding_days_reached():
    assert holding_days_reached(days_held=7, holding_days=7)


def test_holding_days_passed():
    assert holding_days_reached(days_held=8, holding_days=7)


def test_holding_days_not_reached():
    assert not holding_days_reached(days_held=6, holding_days=7)
