package src.main.java.EntryPoint;

import src.main.java.LOB.LimitOrder;
import src.main.java.LOB.MarketOrder;
import src.main.java.LOB.OrderEnum;
import src.main.java.LOB.LimitOrderGroup;

record Tester(int lmao, double lol) {

}

public class Main {
    public static void main(String[] args){

        LimitOrderGroup priceGroup = new LimitOrderGroup(120.0, OrderEnum.Side.BID);

        for (int i = 0; i < 10; ++i){
            priceGroup.addOrder(new LimitOrder(120, 10., OrderEnum.Side.BID, 12));
        }

        System.out.println(priceGroup);

        MarketOrder marketOrder = new MarketOrder(25, OrderEnum.Side.ASK, 0);

        System.out.println("Purchased with " + marketOrder);
        priceGroup.takeOrder(marketOrder);
        
        System.out.println(priceGroup);

        // for (Order o: priceGroup.ordersDeque){
        //     System.out.println(o);
        // }
        // Order o = new Order(12.4, 10., OrderEnum.Side.BID , OrderEnum.Type.LIMIT, 12);
        
    }
}
