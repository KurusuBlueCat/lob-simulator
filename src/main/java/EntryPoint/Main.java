
package EntryPoint;

import LOB.LimitOrder;
// import LOB.MarketOrder;
import LOB.OrderEnum;
import LOB.LimitOrderBook;

record Tester(int lmao, double lol) {

}

public class Main {
    public static void main(String[] args){

        LimitOrderBook LOB = new LimitOrderBook();

        for (int i = 0; i < 10; ++i){
            LOB.receiveLimitOrder(new LimitOrder(i*10, 10., OrderEnum.Side.BID));
            System.out.println(LOB);
        }

    }
}

