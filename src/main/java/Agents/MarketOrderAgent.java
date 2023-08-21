package Agents;

import java.util.Random;

import LOB.LimitOrderBook;
import LOB.Order.MarketOrder;
import LOB.OrderEnum.OrderCompletedMsg;
import LOB.OrderEnum.Side;

public class MarketOrderAgent extends Agent {
    private float _sideBias;
    private double _amount;
    private Random _rng;

    public MarketOrderAgent(LimitOrderBook LOB, double sideBias,
                            double amount, long seed, long id){
        super(LOB);
        this._sideBias = (float)sideBias;
        this._amount = amount;

        _rng = new Random();
        _rng.setSeed(seed);
    }

    public MarketOrderAgent(LimitOrderBook LOB, long id){
        this(LOB, (float)0.5, 1., 0, id);
    }

    public void act(){
        MarketOrder newOrder;
        if (_rng.nextFloat() < _sideBias){
            newOrder = new MarketOrder(_amount, Side.ASK);
        } else {
            newOrder = new MarketOrder(_amount, Side.BID);
        }

        sendOrder(newOrder);
    }

    public void completeOrder(long id, OrderCompletedMsg msg){
        switch (msg) {
            case FILLED:
                liveOrders.remove(id);
                break;
            case CANCELLED:
                liveOrders.remove(id);
                break;
        }
    }

}
