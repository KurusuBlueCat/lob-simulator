package src.main.java.LOB;

public class MarketOrder extends Order{
    public MarketOrder(double amount, OrderEnum.Side side, int id) {
        super(amount, side, id);
    }
}
