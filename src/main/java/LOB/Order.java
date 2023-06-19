package src.main.java.LOB;

import java.time.Instant;
import java.util.Comparator;

public class Order {
    int id;
    public double amount;
    // double price;
    OrderEnum.Side side;
    long timestamp;

    public static Comparator<Order> TimeComparator = Comparator.comparingLong(Order::getTimestamp);
    // public static Comparator<Order> PriceComparator = Comparator.comparingDouble(Order::getPrice);

    public Order(double amount, OrderEnum.Side side, int id) {
        // this.price = price;
        this.amount = amount;
        this.side = side;
        this.id = id;

        setTimestamp();
    }

    public void setTimestamp(long unixTimeNano){
        this.timestamp = unixTimeNano;
    }

    public void setTimestamp(){
        Instant now = Instant.now();
        setTimestamp(now.toEpochMilli()*1_000_000 + now.getNano());
    }

    public long getTimestamp() {
        return timestamp;
    }

    // public double getPrice() {
    //     return price;
    // }

    @Override
    public String toString() {
        // Printer
        return side + " " + timestamp
               + "id: " + id
            //    + "price: " + price
               + "amount: " + amount;
    }
}