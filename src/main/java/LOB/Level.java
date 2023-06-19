package src.main.java.LOB;

/**
 * This is a record for LOB price level
 */
public record Level(double price, double size, int orderCount, OrderEnum.Side side) {

    @Override
    public String toString(){
        return String.format("%s [%-10s, %-10s, %-5s]", side, price, size, orderCount);
    }
}