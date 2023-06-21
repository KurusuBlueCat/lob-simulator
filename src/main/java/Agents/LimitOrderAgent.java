package Agents;

import java.util.HashSet;
import java.util.Random;

import LOB.LimitOrder;
import LOB.LimitOrderBook;
import LOB.OrderEnum.Side;


public class LimitOrderAgent extends Agent {
    public double priceIncrement;
    public HashSet<Long> idSet;
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

        idSet = new HashSet<Long>();
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

        idSet.add(LOB.receiveLimitOrder(new LimitOrder(orderPrice, _amount, side)));

        for (Long toRemove : idSet){
            if (_rng.nextDouble() < _cancelRate)
                LOB.cancelOrder(toRemove);
        }
    }

    private int exponentialInt(){
        return (int)(-_lambda * Math.log(1 - _rng.nextFloat()));
    }
}