package LOB;

import java.util.TreeSet;

import Agents.Agent;
import Agents.DummyAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import LOB.Order.LimitOrder;
import LOB.Order.MarketOrder;
import LOB.OrderEnum.OrderCompletedMsg;
import LOB.OrderEnum.Side;
import LOB.Utils.IDMaker;

public class LimitOrderBook {
    TreeSet<LimitOrderGroup> asks;
    TreeSet<LimitOrderGroup> bids;

    /**
     * id-limitorder mapping
     */
    HashMap<Long, LimitOrder> idOrderMap;

    /**
     * id-agent mapping
     */
    HashMap<Long, Agent> idAgentMap;

    /**
     * id-id mapping
     */
    HashMap<Long, Long> orderAgentMap;

    private int _maxPrint=10;

    // double minimumIncrement;
    // double minimumAmount;

    private IDMaker _order_idm;
    private IDMaker _agent_idm;
    private double _latestBestBid;
    private double _latestBestAsk;

    public LimitOrderBook(){
        asks = new TreeSet<LimitOrderGroup>();
        bids = new TreeSet<LimitOrderGroup>(LimitOrderGroup.PriceComparator.reversed());
        idOrderMap = new HashMap<Long, LimitOrder>();
        idAgentMap = new HashMap<Long, Agent>();
        orderAgentMap = new HashMap<Long, Long>();
        _latestBestBid = Double.MIN_VALUE;
        _latestBestAsk = Double.MAX_VALUE;
        _order_idm = new IDMaker();
        _agent_idm = new IDMaker();

        DummyAgent dagent = new DummyAgent(this);
        registerAgent(-1, dagent); //create dummy agent to -1
    }

    public double getBestBid(){
        return _latestBestBid;
    }

    private void updateBestBid(){
        _latestBestBid = Double.max(_latestBestBid, bids.first().getPrice());
    }

    public double getBestAsk(){
        return _latestBestAsk;
    }

    private void updateBestAsk(){
        _latestBestAsk = Double.min(_latestBestAsk, asks.first().getPrice());
    }

    public TreeSet<LimitOrderGroup> getBids(){
        return bids;
    }

    public TreeSet<LimitOrderGroup> getAsks(){
        return asks;
    }

    protected void registerAgent(long agentID, Agent agent){
        idAgentMap.put(agentID, agent);
    }

    public long registerAgent(Agent agent){
        long newID = _agent_idm.makeID();
        registerAgent(newID, agent);
        return newID;
    }

    public long receiveLimitOrder(LimitOrder order){
        return receiveLimitOrder(order, -1);
    }

    public long receiveLimitOrder(LimitOrder order, long agentID){
        long orderID = _order_idm.makeID();
        try{
            order.setID(orderID);
        } catch (IllegalStateException e) {
            return -1; //this indicates that Order was invalid/already stamped with ID
        }

        boolean isAsk = order.side == Side.ASK;

        TreeSet<LimitOrderGroup> groupSet = isAsk ? asks : bids;

        LimitOrderGroup orderGroup = groupSet.floor(new LimitOrderGroup(order.price, order.side));

        if ((orderGroup == null) || (orderGroup.getPrice() != order.getPrice())) {
            orderGroup = new LimitOrderGroup(order.price, order.side);
            groupSet.add(orderGroup);
        }

        orderGroup.addOrder(order);
        idOrderMap.put(orderID, order);
        orderAgentMap.put(orderID, agentID);

        if (isAsk){
            updateBestAsk();
        } else {
            updateBestBid();
        }

        return orderID;
    }

    public double receiveMarketOrder(MarketOrder order){
        receiveMarketOrder(order, -1);
        return order.vwap;
    }

    /**
     * Currently this function immediately fills market order. There are no
     * implementation for unfilled market order yet.
     * 
     * @param order A MarketOrder to be matched with existing LimitOrder
     * 
     * @return order id
     */
    public long receiveMarketOrder(MarketOrder order, long agentID){
        long newID = _order_idm.makeID();
        try{
            order.setID(newID);
        } catch (IllegalStateException e) {
            return -1; //this indicates that Order was invalid/already stamped with ID
        }

        boolean isAsk = order.side == Side.ASK;
        TreeSet<LimitOrderGroup> limitOrders = isAsk ? bids : asks; //take oppposite limit order

        double totalValue = 0; //this...
        double totalAmount = order.amount; //...and this are for VWAP calculation
        double startingAmount;
        double amountTraded;

        ArrayList<LimitOrderGroup> groupToRemove = new ArrayList<LimitOrderGroup>();

        for (LimitOrderGroup lOGroup : limitOrders){
            startingAmount = order.amount;
            order = lOGroup.takeOrder(order);

            for (long idToRemove : lOGroup.getRemoved()){
                idOrderMap.remove(idToRemove);
                long orderOwnerID = orderAgentMap.get(idToRemove);
                idAgentMap.get(orderOwnerID).completeOrder(idToRemove, OrderCompletedMsg.FILLED);
            }

            if (lOGroup.aggregateOrderAmount() == 0){
                groupToRemove.add(lOGroup);
            }
            amountTraded = startingAmount - order.amount;

            totalValue += amountTraded * lOGroup.price;
            if (order.amount == 0){
                break;
            }
        }

        order.vwap = totalValue/totalAmount;

        for (LimitOrderGroup r : groupToRemove){
            limitOrders.remove(r);
        }

        return -1; //return -1 because market order would have been filled.
        //Will have to improve this part
    }

    public boolean cancelOrder(long id){
        if (!idOrderMap.containsKey(id)) return false;

        LimitOrderGroup priceLevel = new LimitOrderGroup(idOrderMap.get(id).price, null);
        //no crossed market assumption
        boolean isAsk = priceLevel.price > getBestBid();
        LimitOrderGroup group = isAsk ? asks.ceiling(priceLevel)
                                      : bids.floor(priceLevel);

        if (group == null) return false;

        boolean result = group.cancelOrder(id);

        long orderOwnerID = orderAgentMap.get(id);
        idAgentMap.get(orderOwnerID).completeOrder(id, OrderCompletedMsg.CANCELLED);

        if (group.countOrder() == 0) {
            if (isAsk) asks.remove(group); else bids.remove(group);
        }

        return result;
    }

    public void set_maxPrint(int maxPrint){
        this._maxPrint = maxPrint;
    }

    @Override
    public String toString() {
        Iterator<LimitOrderGroup> aIter = asks.iterator();
        Iterator<LimitOrderGroup> bIter = bids.iterator();
        LimitOrderGroup a = aIter.hasNext() ? aIter.next(): null;
        LimitOrderGroup b = bIter.hasNext() ? bIter.next(): null;

        ArrayList<String> toJoin = new ArrayList<String>();

        toJoin.add("Limit Order Book");

        for (int i=0; i<_maxPrint; ++i)  {
            toJoin.add(LimitOrderGroup.toStringNull(b) + " " + LimitOrderGroup.toStringNull(a));

            a = aIter.hasNext() ? aIter.next(): null;
            b = bIter.hasNext() ? bIter.next(): null;

            if (!((a != null) || (b != null))){
                break;
            }
        }

        return String.join("\n", toJoin);
    }

    public String showIDMap() {
        return idOrderMap.toString();
    }

}
