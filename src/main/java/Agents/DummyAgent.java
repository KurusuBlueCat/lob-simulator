package Agents;

import LOB.OrderEnum.OrderCompletedMsg;
import LOB.LimitOrderBook;

public class DummyAgent extends Agent {

    public DummyAgent(LimitOrderBook LOB){
        super(LOB);
    }

    public void act(){};
    public void completeOrder(long id, OrderCompletedMsg msg){};
}
