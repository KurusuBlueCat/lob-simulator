package LOB;

import org.testng.Assert;
import org.testng.annotations.Test;

import LOB.Order.LimitOrder;

public class LimitOrderGroupTests {
    
    @Test
    public void testPopulate(){
        LimitOrderGroup priceGroup = new LimitOrderGroup(120.0, OrderEnum.Side.BID);

        for (int i = 0; i < 10; ++i){
            priceGroup.addOrder(new LimitOrder(120, 10., OrderEnum.Side.BID));
        }

        Assert.assertEquals(priceGroup.getPrice(), 120.0);
        Assert.assertEquals(priceGroup.countOrder(), 10);
        Assert.assertEquals(priceGroup.aggregateOrderAmount(), 100.0);
    }
}
