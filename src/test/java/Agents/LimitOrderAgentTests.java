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

        LimitOrderAgent a = new LimitOrderAgent(LOB);

        a.sendOrder(new LimitOrder(990, 5., OrderEnum.Side.BID));
        a.sendOrder(new LimitOrder(1010, 5., OrderEnum.Side.ASK));
        
        Assert.assertEquals(a.liveOrders.size(), 2);

        Assert.assertEquals(LOB.getAsks().size(), 1);
        Assert.assertEquals(LOB.getBids().size(), 1);
        Assert.assertEquals(LOB.getBestBid(), 990);
    }

    // @Test
    // public void matchOrderTest() {

    //     LimitOrderBook LOB = new LimitOrderBook();
    //     Agent a = new LimitOrderAgent(LOB);

    //     a.submitOrder(new LimitOrder(990, 5., OrderEnum.Side.BID));
    //     a.submitOrder(new LimitOrder(1010, 5., OrderEnum.Side.ASK));        

    //     a.submitOrder();

    //     //With seed = 0 (by default), the agent will place a Bid order at 1009.7
    //     Assert.assertEquals(LOB.getAsks().size(), 1);
    //     Assert.assertEquals(LOB.getBids().size(), 2);
    //     Assert.assertEquals(LOB.getBestBid(), 1009.700);
    // }
    
}
