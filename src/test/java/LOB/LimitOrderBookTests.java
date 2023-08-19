package LOB;

import org.testng.Assert;
import org.testng.annotations.Test;

import LOB.Order.LimitOrder;

public class LimitOrderBookTests {

    @Test
    public void testMultipleBidLevels(){
        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {10.0, 0.0, 20.0, 40.0, 30.0};
        Double[] orderedPrice = {40.0, 30.0, 20.0, 10.0, 0.0};

        for (double price : randomOrder){
            LOB.receiveLimitOrder(new LimitOrder(price, 10., OrderEnum.Side.BID));
        }

        Assert.assertEquals(LOB.getBids().size(), 5);

        int counter = 0;
        for (LimitOrderGroup bidGroup : LOB.getBids()){
            Assert.assertEquals(bidGroup.aggregateOrderAmount(), 10.0);
            Assert.assertTrue(bidGroup.getPrice() == orderedPrice[counter]);
            ++counter;
        }

        Assert.assertEquals(LOB.getBestBid(), 40.0);
    }

    @Test
    public void testMultipleAskLevels(){
        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 70.0, 60.0, 90.0, 80.0};
        Double[] orderedPrice = {50.0, 60.0, 70.0, 80.0, 90.0};

        for (double price : randomOrder){
            LOB.receiveLimitOrder(new LimitOrder(price, 10., OrderEnum.Side.ASK));
        }

        Assert.assertEquals(LOB.getAsks().size(), 5);

        int counter = 0;
        for (LimitOrderGroup bidGroup : LOB.getBids()){
            Assert.assertEquals(bidGroup.aggregateOrderAmount(), 10.0);
            Assert.assertTrue(bidGroup.getPrice() == orderedPrice[counter]);
            ++counter;
        }

        Assert.assertEquals(LOB.getBestAsk(), 50.0);
    }

    @Test
    public void testSingleBidLevel(){
        LimitOrderBook LOB = new LimitOrderBook();

        for (int i=0; i<10; ++i){
            LOB.receiveLimitOrder(new LimitOrder(100., 10., OrderEnum.Side.BID));
        }

        Assert.assertEquals(LOB.getBids().size(), 1);
        Assert.assertEquals(LOB.getBids().first().price, 100.);
        Assert.assertEquals(LOB.getBids().first().countOrder(), 10);
        Assert.assertEquals(LOB.getAsks().size(), 0);
    }

    @Test
    public void testSingleAskLevel(){
        LimitOrderBook LOB = new LimitOrderBook();

        for (int i=0; i<10; ++i){
            LOB.receiveLimitOrder(new LimitOrder(100., 10., OrderEnum.Side.ASK));
        }

        Assert.assertEquals(LOB.getAsks().size(), 1);
        Assert.assertEquals(LOB.getAsks().first().price, 100.);
        Assert.assertEquals(LOB.getAsks().first().countOrder(), 10);
        Assert.assertEquals(LOB.getBids().size(), 0);
    }

    @Test
    public void cancelBidNotEmpty(){
        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 70.0, 60.0, 90.0, 80.0};

        for (double price : randomOrder){
            LOB.receiveLimitOrder(new LimitOrder(price, 20., OrderEnum.Side.BID));
            LOB.receiveLimitOrder(new LimitOrder(price, 20., OrderEnum.Side.BID));
        }

        LOB.cancelOrder(2);
        Assert.assertEquals(LOB.getBids().floor(new LimitOrderGroup(70, null)).countOrder(), 1);
        Assert.assertEquals(LOB.getBids().size(), 5);
    }
    
    @Test
    public void cancelAskNotEmpty(){
        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 70.0, 60.0, 90.0, 80.0};

        for (double price : randomOrder){
            LOB.receiveLimitOrder(new LimitOrder(price, 20., OrderEnum.Side.ASK));
            LOB.receiveLimitOrder(new LimitOrder(price, 20., OrderEnum.Side.ASK));
        }

        LOB.cancelOrder(2);
        Assert.assertEquals(LOB.getAsks().floor(new LimitOrderGroup(70, null)).countOrder(), 1);
        Assert.assertEquals(LOB.getAsks().size(), 5);
    }

    @Test
    public void cancelAskEmpty(){
        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 70.0, 60.0, 90.0, 80.0};

        for (double price : randomOrder){
            LOB.receiveLimitOrder(new LimitOrder(price, 20., OrderEnum.Side.ASK));
        }

        LOB.cancelOrder(2);
        Assert.assertEquals(LOB.getAsks().floor(new LimitOrderGroup(60, null)).getPrice(), 50);
        Assert.assertEquals(LOB.getAsks().size(), 4);
    }

    @Test
    public void cancelBidEmpty(){
        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 70.0, 60.0, 90.0, 80.0};

        for (double price : randomOrder){
            LOB.receiveLimitOrder(new LimitOrder(price, 20., OrderEnum.Side.BID));
        }

        LOB.cancelOrder(2);
        // floor is 70 because bid order are reversed from highest to lowest
        Assert.assertEquals(LOB.getBids().floor(new LimitOrderGroup(60, null)).getPrice(), 70);
        Assert.assertEquals(LOB.getBids().size(), 4);
    }
}
