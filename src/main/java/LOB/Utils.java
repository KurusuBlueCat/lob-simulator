package LOB;

public class Utils { //Namespace
    
public static class IDMaker {
    private long currentID;

    public IDMaker(){
        currentID = -1;
    }

    public long makeID(){
        //TODO: make this atomic
        ++currentID;
        if (currentID < 0){
            currentID = 0;
        }
        return currentID;
    }
}

}
