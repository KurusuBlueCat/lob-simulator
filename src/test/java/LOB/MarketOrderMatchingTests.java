package LOB;

import org.testng.Assert;
import org.testng.annotations.Test;

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
}
