package Agents;

import org.testng.Assert;
import org.testng.annotations.Test;

import LOB.LimitOrderBook;
import LOB.OrderEnum;
import LOB.Order.LimitOrder;

public class LimitOrderAgentTests {
    @Test
    public void placeOrderTests() {

        LimitOrderBook LOB = new LimitOrderBook();

        LOB.receiveLimitOrder(new LimitOrder(990, 5., OrderEnum.Side.BID));
        LOB.receiveLimitOrder(new LimitOrder(1010, 5., OrderEnum.Side.ASK));

        Agent a = new LimitOrderAgent(LOB);

        a.act();

        //With seed = 0 (by default), the agent will place a Bid order at 1009.7
        Assert.assertEquals(LOB.getAsks().size(), 1);
        Assert.assertEquals(LOB.getBids().size(), 2);
        Assert.assertEquals(LOB.getBestBid(), 1009.700);
    }
    
}
