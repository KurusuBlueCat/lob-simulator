
package EntryPoint;

import LOB.LimitOrder;
// import LOB.MarketOrder;
import LOB.OrderEnum;
import LOB.LimitOrderBook;
import LOB.LimitOrderGroup;
import LOB.MarketOrder;

record Tester(int lmao, double lol) {

}

public class Main {
    public static void main(String[] args){

        LimitOrderBook LOB = new LimitOrderBook();

        Double[] randomOrder = {50.0, 70.0, 60.0, 90.0, 80.0};

        for (double price : randomOrder){
            LOB.receiveLimitOrder(new LimitOrder(price, 20., OrderEnum.Side.BID));
        }

        System.out.println(LOB.getBids());
        // System.out.println(LOB);
        // System.out.println(LOB.getBids().floor(new LimitOrderGroup(60, null)).getPrice());
        // System.out.println(LOB.getBids().size());
    }
}

