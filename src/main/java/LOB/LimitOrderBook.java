package LOB;

import java.util.TreeSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import LOB.OrderEnum.Side;
import LOB.Utils.IDMaker;

public class LimitOrderBook {
    TreeSet<LimitOrderGroup> asks;
    TreeSet<LimitOrderGroup> bids;
    HashMap<Long, Double> idPriceMap;
    // double minimumIncrement;
    // double minimumAmount;

    private IDMaker _idm;
    private double _latestBestBid;
    private double _latestBestAsk;

    public LimitOrderBook(){
        asks = new TreeSet<LimitOrderGroup>();
        bids = new TreeSet<LimitOrderGroup>(LimitOrderGroup.PriceComparator.reversed());
        idPriceMap = new HashMap<Long, Double>();
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
        idPriceMap.put(newID, order.price);

        if (isAsk){
            updateBestAsk();
        } else {
            updateBestBid();
        }

        return newID;
    }

    // public receiveMarketOrder()

    @Override
    public String toString() {
        Iterator<LimitOrderGroup> aIter = asks.iterator();
        Iterator<LimitOrderGroup> bIter = bids.iterator();
        LimitOrderGroup a = aIter.hasNext() ? aIter.next(): null;
        LimitOrderGroup b = bIter.hasNext() ? bIter.next(): null;

        ArrayList<String> toJoin = new ArrayList<String>();

        toJoin.add("Limit Order Book");

        while ((a != null) || (b != null)) {
            toJoin.add(LimitOrderGroup.toStringNull(b) + " " + LimitOrderGroup.toStringNull(a));

            a = aIter.hasNext() ? aIter.next(): null;
            b = bIter.hasNext() ? bIter.next(): null;
        } 

        return String.join("\n", toJoin);
    }

}
