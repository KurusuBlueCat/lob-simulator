
package EntryPoint;

import LOB.OrderEnum;
import LOB.Order.LimitOrder;
import LOB.LimitOrderBook;

import Agents.Agent;
// import LOB.LimitOrderGroup;
// import LOB.MarketOrder;
import Agents.LimitOrderAgent;
import Agents.MarketOrderAgent;

record Tester(int lmao, double lol) {

}

public class Main {
    public static void main(String[] args){

        LimitOrderBook LOB = new LimitOrderBook();
        LOB.set_maxPrint(10);

        LOB.receiveLimitOrder(new LimitOrder(990, 5., OrderEnum.Side.BID));
        LOB.receiveLimitOrder(new LimitOrder(1010, 5., OrderEnum.Side.ASK));

        System.out.println(LOB);

        Agent[] agentArr = {
            new LimitOrderAgent(LOB, 0.1, 0.5, 50., 10., 0.03, 0),
            new LimitOrderAgent(LOB, 0.1, 0.5, 30., 15., 0.03, 3),
            new LimitOrderAgent(LOB, 0.1, 0.5, 10., 6., 0.03, 9),
            new MarketOrderAgent(LOB, 0.5, 3, 4, 3),
            new MarketOrderAgent(LOB, 0.5, 4, 5, 4),
        };

        for (int i=0; i<150; ++i){
            agentArr[0].act();
        }

        // for (int i=0; i<150; ++i){
        for (;;){
            for (Agent a: agentArr) {
                a.act();
                System.out.println(LOB);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // // System.out.println(LOB.getBids());
        // System.out.println(LOB.getAsks().size());
        // System.out.println(LOB.getBids().size());
        // System.out.println(LOB.getBids().floor(new LimitOrderGroup(60, null)).getPrice());
        // System.out.println(LOB.getBids().size());
    }
}
