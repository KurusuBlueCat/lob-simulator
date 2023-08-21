package LOB;

import org.testng.Assert;
import org.testng.annotations.Test;

import LOB.Order.LimitOrder;
import LOB.Order.MarketOrder;

public class MarketOrderMatchingTests {
    LimitOrderGroup getTenBidOrderGroup(){
        LimitOrderGroup priceGroup = new LimitOrderGroup(120.0, OrderEnum.Side.BID);

        for (int i = 0; i < 10; ++i){
            priceGroup.addOrder(new LimitOrder(120, 10., OrderEnum.Side.BID));
        }

        return priceGroup;
    }

    @Test
    public void testTakeOrder() {
        LimitOrderGroup priceGroup = getTenBidOrderGroup();

        MarketOrder marketOrder = new MarketOrder(25, OrderEnum.Side.ASK);

        priceGroup.takeOrder(marketOrder);
        
        Assert.assertEquals(marketOrder.amount, 0);
        Assert.assertEquals(priceGroup.getPrice(), 120.0);
        Assert.assertEquals(priceGroup.aggregateOrderAmount(), 75.0);
        Assert.assertEquals(priceGroup.countOrder(), 8);
        Assert.assertEquals(priceGroup.ordersDeque.peek().amount, 5.0);
    }

    @Test
    public void testTakeBigOrder() {
        LimitOrderGroup priceGroup = getTenBidOrderGroup();

        MarketOrder marketOrder = new MarketOrder(105, OrderEnum.Side.ASK);

        priceGroup.takeOrder(marketOrder);
        
        Assert.assertEquals(marketOrder.amount, 5);
        Assert.assertEquals(priceGroup.getPrice(), 120.0);
        Assert.assertEquals(priceGroup.aggregateOrderAmount(), 0.0);
        Assert.assertEquals(priceGroup.countOrder(), 0);
    }

    @Test
    public void testTakeBidFromLOB(){
        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 70.0, 60.0, 90.0, 80.0};

        for (double price : randomOrder){
            LOB.receiveLimitOrder(new LimitOrder(price, 10., OrderEnum.Side.BID));
        }

        MarketOrder marketOrder = new MarketOrder(30, OrderEnum.Side.ASK);

        double avgPrice = LOB.receiveMarketOrder(marketOrder);

        Assert.assertEquals(LOB.getBids().size(), 2);
        Assert.assertEquals(avgPrice, 80.0);
    }

    @Test
    public void testTakeAskFromLOB(){
        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 70.0, 60.0, 90.0, 80.0};

        for (double price : randomOrder){
            LOB.receiveLimitOrder(new LimitOrder(price, 10., OrderEnum.Side.ASK));
        }

        MarketOrder marketOrder = new MarketOrder(30, OrderEnum.Side.BID);

        double avgPrice = LOB.receiveMarketOrder(marketOrder);

        Assert.assertEquals(LOB.getAsks().size(), 2);
        Assert.assertEquals(avgPrice, 60.0);
    }

    @Test
    public void testTakeBidFromLOB2(){
        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 70.0, 60.0, 90.0, 80.0};

        for (double price : randomOrder){
            LOB.receiveLimitOrder(new LimitOrder(price, 10., OrderEnum.Side.BID));
            LOB.receiveLimitOrder(new LimitOrder(price, 10., OrderEnum.Side.BID));
        }

        MarketOrder marketOrder = new MarketOrder(30, OrderEnum.Side.ASK);

        double avgPrice = LOB.receiveMarketOrder(marketOrder);

        Assert.assertTrue((avgPrice - 86.66666666666667) < Constants.EPSILON);
        Assert.assertEquals(LOB.bids.first().countOrder(), 1);
        Assert.assertEquals(LOB.bids.first().aggregateOrderAmount(), 10.0);
    }

    @Test
    public void testTakeAskFromLOB2(){
        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 70.0, 60.0, 90.0, 80.0};

        for (double price : randomOrder){
            LOB.receiveLimitOrder(new LimitOrder(price, 10., OrderEnum.Side.ASK));
            LOB.receiveLimitOrder(new LimitOrder(price, 10., OrderEnum.Side.ASK));
        }

        MarketOrder marketOrder = new MarketOrder(30, OrderEnum.Side.BID);

        double avgPrice = LOB.receiveMarketOrder(marketOrder);

        Assert.assertTrue((avgPrice - 53.33333333333) < Constants.EPSILON);
        Assert.assertEquals(LOB.asks.first().countOrder(), 1);
        Assert.assertEquals(LOB.asks.first().aggregateOrderAmount(), 10.0);
    }

    @Test
    public void timePriorityAskTest(){
        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 50.0, 50.0, 50.0, 50.0};
        long[] orderIDs = new long[5];

        for (int i=0; i<5; ++i){
            double price = randomOrder[i];
            long id = LOB.receiveLimitOrder(new LimitOrder(price, 10., OrderEnum.Side.ASK));
            orderIDs[i] = id;
        }

        LOB.receiveMarketOrder(new MarketOrder(30, OrderEnum.Side.BID));

        //This checks if the remaining order ids are orderIDs[3] and orderIDs[4]
        for (int i=3; i<5; ++i){
            Assert.assertEquals(LOB.getAsks().first().ordersDeque.peek().id, 
                                orderIDs[i]);
            //match the above order and remove from the LOB
            LOB.receiveMarketOrder(new MarketOrder(10, OrderEnum.Side.BID));
        }

        //There should be no other orders
        Assert.assertEquals(LOB.getAsks().size(), 0);
    }

    @Test
    public void timePriorityBidTest(){
        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 50.0, 50.0, 50.0, 50.0};
        long[] orderIDs = new long[5];

        for (int i=0; i<5; ++i){
            double price = randomOrder[i];
            long id = LOB.receiveLimitOrder(new LimitOrder(price, 10., OrderEnum.Side.BID));
            orderIDs[i] = id;
        }

        LOB.receiveMarketOrder(new MarketOrder(30, OrderEnum.Side.ASK));

        //This checks if the remaining order ids are orderIDs[3] and orderIDs[4]
        for (int i=3; i<5; ++i){
            Assert.assertEquals(LOB.getBids().first().ordersDeque.peek().id, 
                                orderIDs[i]);
            //match the above order and remove from the LOB
            LOB.receiveMarketOrder(new MarketOrder(10, OrderEnum.Side.ASK));
        }

        //There should be no other orders
        Assert.assertEquals(LOB.getBids().size(), 0);
    }
}
