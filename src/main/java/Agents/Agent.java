package Agents;

import java.util.HashMap;
import LOB.LimitOrderBook;
import LOB.Interfaces.HasID;
import LOB.Order.LimitOrder;
import LOB.Order.MarketOrder;
import LOB.Order.Order;
import LOB.OrderEnum.OrderCompletedMsg;

public abstract class Agent implements HasID {
    protected LimitOrderBook LOB;
    public HashMap<Long, Order> liveOrders;
    protected long id;
    private boolean _idLocked = false;

    public Agent(LimitOrderBook LOB){
        this.LOB = LOB;
        setID(LOB.registerAgent(this));
        this.liveOrders = new HashMap<Long, Order>();
    }

    public void sendOrder(LimitOrder order){
        long orderID = LOB.receiveLimitOrder(order, this.id);
        liveOrders.put(orderID, order);
    }

    public void sendOrder(MarketOrder order){
        LOB.receiveMarketOrder(order, this.id);
    }

    public long getID(){
        return this.id;
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

    abstract public void act();
    abstract public void completeOrder(long id, OrderCompletedMsg msg);
}