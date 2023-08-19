package LOB.Order;

import LOB.OrderEnum;

public class MarketOrder extends Order{
    public double vwap;

    public MarketOrder(double amount, OrderEnum.Side side) {
        super(amount, side);
    }
}
