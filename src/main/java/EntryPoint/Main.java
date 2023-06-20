
package EntryPoint;

import LOB.LimitOrder;
// import LOB.MarketOrder;
import LOB.OrderEnum;
import LOB.LimitOrderBook;
import LOB.MarketOrder;

record Tester(int lmao, double lol) {

}

public class Main {
    public static void main(String[] args){

        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 70.0, 60.0, 90.0, 80.0};

        for (double price : randomOrder){
            LOB.receiveLimitOrder(new LimitOrder(price, 20., OrderEnum.Side.BID));
            System.out.println(LOB);
        }

        MarketOrder marketOrder = new MarketOrder(30, OrderEnum.Side.ASK);

        double avgPrice = LOB.receiveMarketOrder(marketOrder);

        System.out.println(LOB);
        System.out.println(avgPrice);
    }
}

