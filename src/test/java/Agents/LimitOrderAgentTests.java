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

    @Test
    public void randomLimitOrderTest() {

        LimitOrderBook LOB = new LimitOrderBook();
        Agent a = new LimitOrderAgent(LOB);

        a.sendOrder(new LimitOrder(990, 5., OrderEnum.Side.BID));
        a.sendOrder(new LimitOrder(1010, 5., OrderEnum.Side.ASK));

        a.act();

        //With seed = 0 (by default), the agent will place a Bid order at 1009.7
        Assert.assertEquals(LOB.getAsks().size(), 1);
        Assert.assertEquals(LOB.getBids().size(), 2);
        Assert.assertEquals(LOB.getBestBid(), 1009.700);
    }

    @Test
    public void cancelOrderTest() {
        LimitOrderBook LOB = new LimitOrderBook();
        Agent a = new LimitOrderAgent(LOB);

        long orderID = a.sendOrder(new LimitOrder(990, 5., OrderEnum.Side.BID));
        long orderID2 = a.sendOrder(new LimitOrder(1000, 5., OrderEnum.Side.ASK));

        //Expected Initial State
        Assert.assertTrue(a.liveOrders.containsKey(orderID));
        Assert.assertTrue(a.liveOrders.containsKey(orderID2));
        Assert.assertEquals(a.liveOrders.size(), 2);
        Assert.assertEquals(LOB.getBids().size(), 1);
        Assert.assertEquals(LOB.getAsks().size(), 1);
        Assert.assertEquals(LOB.getBids().first().ordersDeque.peek().id, orderID);
        Assert.assertEquals(LOB.getAsks().first().ordersDeque.peek().id, orderID2);

        //This evaluates to true if cancel successful
        Assert.assertTrue(a.cancelOrder(orderID));

        //check if the correct order was removed
        Assert.assertTrue(!a.liveOrders.containsKey(orderID));
        Assert.assertTrue(a.liveOrders.containsKey(orderID2));
        Assert.assertEquals(a.liveOrders.size(), 1);
        Assert.assertEquals(LOB.getBids().size(), 0);
        Assert.assertEquals(LOB.getAsks().size(), 1);
        Assert.assertEquals(LOB.getBids().size(), 0);
        Assert.assertEquals(LOB.getAsks().first().ordersDeque.peek().id, orderID2);

        //canceling should fail
        Assert.assertTrue(!a.cancelOrder(orderID));
    }
    
}
