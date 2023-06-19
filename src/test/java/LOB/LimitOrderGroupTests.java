package LOB;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LimitOrderGroupTests {
    
    @Test
    public void testPopulate(){
        LimitOrderGroup priceGroup = new LimitOrderGroup(120.0, OrderEnum.Side.BID);

        for (int i = 0; i < 10; ++i){
            priceGroup.addOrder(new LimitOrder(120, 10., OrderEnum.Side.BID, 12));
        }

        Assert.assertEquals(priceGroup.getPrice(), 120.0);
        Assert.assertEquals(priceGroup.countOrder(), 10);
        Assert.assertEquals(priceGroup.aggregateOrderAmount(), 100.0);
    }
}
