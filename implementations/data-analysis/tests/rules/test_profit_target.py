from data_analysis.rules.profit_target import profit_target_reached


def test_profit_target_reached():
    assert profit_target_reached(entry_close=100, close=105, target_percent=5)


def test_profit_target_passed():
    assert profit_target_reached(entry_close=100, close=106, target_percent=5)


def test_profit_target_not_reached():
    assert not profit_target_reached(entry_close=100, close=104.99, target_percent=5)


def test_loss_misses_target():
    assert not profit_target_reached(entry_close=100, close=95, target_percent=5)
