package Agents;

import java.util.ArrayList;
import LOB.LimitOrderBook;
import LOB.Interfaces.HasID;

public abstract class Agent implements HasID {
    protected LimitOrderBook LOB;
    public ArrayList<Long> liveOrders;
    protected long id;

    public Agent(LimitOrderBook LOB, long id){
        this.LOB = LOB;
        this.liveOrders = new ArrayList<Long>();
        this.id = id;
    }

    public long getID(){
        return this.id;
    }

    abstract public void act();
}