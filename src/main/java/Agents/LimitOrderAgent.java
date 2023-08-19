package Agents;

import java.util.Random;

import LOB.LimitOrderBook;
import LOB.Order.LimitOrder;
import LOB.OrderEnum.Side;
import LOB.OrderEnum.OrderCompletedMsg;


public class LimitOrderAgent extends Agent {
    public double priceIncrement;
    private float _sideBias;
    private double _lambda;
    private double _amount;
    private double _cancelRate;
    private Random _rng;

    public LimitOrderAgent(LimitOrderBook LOB, double priceIncrement, double sideBias, 
                           double lambda, double amount, double cancelRate, long seed){
        super(LOB);
        this.priceIncrement = priceIncrement;
        this._sideBias = (float)sideBias;
        this._lambda = lambda;
        this._amount = amount;
        this._cancelRate = cancelRate;

        _rng = new Random();
        _rng.setSeed(seed);
    }

    public LimitOrderAgent(LimitOrderBook LOB){
        this(LOB, 0.1, (float)0.5, 10.0, 5.0, 0.1, (long)420);
    }

    public void act(){
        Side side = _rng.nextDouble() < _sideBias ? Side.ASK : Side.BID;
        double bestOpposingPrice;
        double orderPrice;

        if (side == Side.ASK){
            bestOpposingPrice = LOB.getBestBid();
            orderPrice = (exponentialInt() + 1) * priceIncrement + bestOpposingPrice;
        } else {
            bestOpposingPrice = LOB.getBestAsk();
            orderPrice = (-exponentialInt() - 1) * priceIncrement + bestOpposingPrice;
        }

        LimitOrder newOrder = new LimitOrder(orderPrice, _amount, side);

        liveOrders.put(LOB.receiveLimitOrder(newOrder, this.id), newOrder);

        for (Long toRemove : liveOrders.keySet()){
            if (_rng.nextDouble() < _cancelRate)
                LOB.cancelOrder(toRemove);
                liveOrders.remove(toRemove);
        }
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

    private int exponentialInt(){
        return (int)(-_lambda * Math.log(1 - _rng.nextFloat()));
    }
}