
package EntryPoint;

import java.sql.Time;
import java.util.Random;

import LOB.LimitOrder;
import LOB.OrderEnum;
import LOB.LimitOrderBook;

import Agents.Agent;
// import LOB.LimitOrderGroup;
// import LOB.MarketOrder;
import Agents.LimitOrderAgent;

record Tester(int lmao, double lol) {

}

public class Main {
    public static void main(String[] args){

        LimitOrderBook LOB = new LimitOrderBook();
        LOB.set_maxPrint(10);

        LOB.receiveLimitOrder(new LimitOrder(990, 5., OrderEnum.Side.BID));
        LOB.receiveLimitOrder(new LimitOrder(1010, 5., OrderEnum.Side.ASK));

        System.out.println(LOB);

        Agent a = new LimitOrderAgent(LOB);

        for (int i=0; i<50; ++i){
            a.act();
            System.out.println(LOB);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // // System.out.println(LOB.getBids());
        // System.out.println(LOB.getAsks().size());
        // System.out.println(LOB.getBids().size());
        // System.out.println(LOB.getBids().floor(new LimitOrderGroup(60, null)).getPrice());
        // System.out.println(LOB.getBids().size());
    }
}
