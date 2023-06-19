package LOB;

import LOB.Interfaces.HasPrice;

import java.util.Deque;
import java.util.ArrayDeque;

/**
 * Stores Order of the same prices together. Assertion will enforce this.
 * Also requires limit order only, and that the OrderSide must also match.
 */

public class LimitOrderGroup implements HasPrice {
    /**
     * Tolerance for price difference so floating point data won't cause
     * problems
     */
    final static double EPSILON = 0.000001;

    final double price;
    final OrderEnum.Side side;
    public Deque<LimitOrder> ordersDeque;
    
    public LimitOrderGroup(double price, OrderEnum.Side side){
        this.price = price;
        this.side = side;
        this.ordersDeque = new ArrayDeque<LimitOrder>();
    }

    @Override
    public double getPrice(){
        return this.price;
    }

    /**
     * Add Order object to the end of the Deque<Order>, after checking
     * for correctness of its orderType and orderSide. Will also enforce
     * that its price are 'close enough' to the price level of this group.
     * @param order Order object to add
     */
    public void addOrder(LimitOrder order){
        assert order.side == this.side
            : "The order must be of type " + this.side;

        assert Math.abs(order.price - this.price) < EPSILON 
            : "Price level mismatch with this group. This group is " + this.price
              + " but the given Order is priced at: " + order.price;

        //Assuming exchange side, order would be stamped on arrival
        //Makes it easy for us! addLast all the way.
        ordersDeque.add(order);
    }

    /**
     * Highway to the Danger Zone. 
     * 
     * Add order without checking ANYTHING
     * @param order Order object to add
     */
    void dangerousAdd(LimitOrder order){
        ordersDeque.add(order);
    }

    /**
     * Buy until order amount is 0 or this group emptied
     * @param marketOrder 
     * incoming order. Must be of type MARKET and is on the opposing
     * side of this LimitOrderGroup
     */
    public MarketOrder takeOrder(MarketOrder marketOrder){
        assert marketOrder.side != side
            : "Incoming order must be of opposing side! This group is " 
              + side + " but the incoming order is " + marketOrder.side;

        while ((marketOrder.amount > 0) && (this.ordersDeque.size() > 0)){
            marketOrder.amount -= ordersDeque.peek().amount;
            if (marketOrder.amount < 0){
                // if amount is < 0, we should have remaining amount in the first
                // order of ordersDeque
                // we can set it to negative of amount, as that is the remainder
                ordersDeque.peek().amount = -marketOrder.amount;
            } else {
                ordersDeque.pop();
            }
        }

        //apply minimum cap to amount. Can't have negative amount!
        marketOrder.amount = Math.max(marketOrder.amount, 0);

        //return 0 if amount is exhausted. 
        //return amount if this group is exhausted.
        return marketOrder;
    }

    public Level getLevel(){
        double total_amount=0;
        for (Order o : ordersDeque){
            total_amount+=o.amount;
        }
        return new Level(price, total_amount, ordersDeque.size(), side);
    }

    @Override
    public String toString() {
        return this.getLevel().toString();
    }
}

