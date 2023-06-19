package src.main.java.LOB;

import src.main.java.LOB.Interfaces.HasPrice;

public class LimitOrder extends Order implements HasPrice {
    double price;

    public LimitOrder(double price, double amount, OrderEnum.Side side, int id){
        super(amount, side, id);
        setPrice(price);

    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
