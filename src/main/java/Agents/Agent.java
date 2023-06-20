package Agents;

import java.util.ArrayList;
import LOB.LimitOrderBook;

public abstract class Agent {
    protected LimitOrderBook LOB;
    public ArrayList<Long> liveOrders;

    public Agent(LimitOrderBook LOB){
        this.LOB = LOB;
        this.liveOrders = new ArrayList<Long>();
    }

    abstract public void act();
}