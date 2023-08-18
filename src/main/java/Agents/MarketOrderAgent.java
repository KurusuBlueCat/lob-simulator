package Agents;

import java.util.Random;

import LOB.LimitOrderBook;
import LOB.MarketOrder;
import LOB.OrderEnum.Side;

public class MarketOrderAgent extends Agent {
    private float _sideBias;
    private double _amount;
    private Random _rng;

    public MarketOrderAgent(LimitOrderBook LOB, double sideBias,
                            double amount, long seed, long id){
        super(LOB, id);
        this._sideBias = (float)sideBias;
        this._amount = amount;

        _rng = new Random();
        _rng.setSeed(seed);
    }

    public MarketOrderAgent(LimitOrderBook LOB, long id){
        this(LOB, (float)0.5, 1., 0, id);
    }

    public void act(){
        if (_rng.nextFloat() < _sideBias){
            LOB.receiveMarketOrder(new MarketOrder(_amount, Side.ASK));
        } else {
            LOB.receiveMarketOrder(new MarketOrder(_amount, Side.BID));
        }
    }
}
