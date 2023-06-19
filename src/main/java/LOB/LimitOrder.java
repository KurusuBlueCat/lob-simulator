package LOB;

import LOB.Interfaces.HasPrice;

public class LimitOrder extends Order implements HasPrice, Comparable<HasPrice> {
    double price;

    public LimitOrder(double price, double amount, OrderEnum.Side side){
        super(amount, side);
        setPrice(price);

    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public int compareTo(HasPrice other) {
        return HasPrice.PriceComparator.compare(this, other);
    }
}
