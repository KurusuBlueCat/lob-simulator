package LOB;

import java.time.Instant;
import java.util.Comparator;

import LOB.Interfaces.HasID;

public class Order implements HasID {
    public long id;
    public double amount;
    // double price;
    OrderEnum.Side side;
    long timestamp;

    public static Comparator<Order> TimeComparator = Comparator.comparingLong(Order::getTimestamp);
    // public static Comparator<Order> PriceComparator = Comparator.comparingDouble(Order::getPrice);

    private boolean _idLocked = false;

    public Order(double amount, OrderEnum.Side side) {
        // this.price = price;
        this.amount = amount;
        this.side = side;

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

    /**
     * permanently stamp an ID to this Order
     * 
     * @param id : long integer to be used as ID
     * 
     * @throw IllegalStateException : 
     * Will be raised if ID was already set
     */
    public void setID(long id) {
        if (_idLocked) {
            throw new IllegalStateException("Id cannot be changed once it is set.");
        } else {
            this.id = id;
        }
    }

    @Override
    public long getID(){
        return this.id;
    }

    @Override
    public String toString() {
        // Printer
        return this.getClass().getSimpleName() + " " + side 
               + " id: " + id
               + " amount: " + amount
               + " " + timestamp;
    }
}