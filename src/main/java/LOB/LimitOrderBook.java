package LOB;

import java.util.TreeSet;

import Agents.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import LOB.OrderEnum.Side;
import LOB.Utils.IDMaker;

public class LimitOrderBook {
    TreeSet<LimitOrderGroup> asks;
    TreeSet<LimitOrderGroup> bids;
    HashMap<Long, LimitOrder> idOrderMap;
    HashMap<Long, Agent> idAgentMap;

    private int _maxPrint=10;

    // double minimumIncrement;
    // double minimumAmount;

    private IDMaker _idm;
    private double _latestBestBid;
    private double _latestBestAsk;

    public LimitOrderBook(){
        asks = new TreeSet<LimitOrderGroup>();
        bids = new TreeSet<LimitOrderGroup>(LimitOrderGroup.PriceComparator.reversed());
        idOrderMap = new HashMap<Long, LimitOrder>();
        idAgentMap = new HashMap<Long, Agent>();
        _latestBestBid = Double.MIN_VALUE;
        _latestBestAsk = Double.MAX_VALUE;
        
        _idm = new IDMaker();
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

    public long receiveLimitOrder(LimitOrder order){
        long newID = _idm.makeID();
        try{
            order.setID(newID);
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
        idOrderMap.put(newID, order);

        if (isAsk){
            updateBestAsk();
        } else {
            updateBestBid();
        }

        return newID;
    }

    /**
     * No support for unfilled market order yet.
     * 
     * This function assumes that market order will be fully filled.
     * 
     * @param order A MarketOrder to be matched with existing LimitOrder
     * 
     * @return VWAP averaged purchasing price.
     */
    public double receiveMarketOrder(MarketOrder order){
        boolean isAsk = order.side == Side.ASK;
        TreeSet<LimitOrderGroup> limitOrders = isAsk ? bids : asks; //take oppposite limit order

        double totalValue = 0;
        double totalAmount = order.amount;
        double startingAmount;
        double amountTraded;

        ArrayList<LimitOrderGroup> toRemove = new ArrayList<LimitOrderGroup>();

        for (LimitOrderGroup lOGroup : limitOrders){
            startingAmount = order.amount;
            order = lOGroup.takeOrder(order);
            for (long idToRemove : lOGroup.getRemoved()){
                idOrderMap.remove(idToRemove);
            }
            if (lOGroup.aggregateOrderAmount() == 0){
                toRemove.add(lOGroup);
            }
            amountTraded = startingAmount - order.amount;

            totalValue += amountTraded * lOGroup.price;
            if (order.amount == 0){
                break;
            }
        }

        for (LimitOrderGroup r : toRemove){
            limitOrders.remove(r);
        }

        return totalValue/totalAmount; // return averaged purchased price.
    }

    public boolean cancelOrder(long id){
        if (!idOrderMap.containsKey(id)) return false;

        LimitOrderGroup priceLevel = new LimitOrderGroup(idOrderMap.get(id).price, null);
        boolean isAsk = priceLevel.price >= getBestBid();
        LimitOrderGroup group = isAsk ? asks.ceiling(priceLevel)
                                      : bids.floor(priceLevel);

        if (group == null) return false;

        boolean result = group.cancelOrder(id);
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
