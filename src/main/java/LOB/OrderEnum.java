package LOB;

public class OrderEnum { //Namespace

public static enum Side{
    BID("Bid"),
    ASK("Ask");

    private final String typename;

    Side(String typename){
        this.typename = typename;
    }

    @Override
    public String toString() {
        // returns a typename as string
        return typename;
    }
}

public static enum OrderCompletedMsg {
    FILLED("Filled"),
    CANCELLED("Cancelled");

    private final String typename;

    OrderCompletedMsg(String typename){
        this.typename = typename;
    }

    @Override
    public String toString() {
        // returns a typename as string
        return typename;
    }
}

}