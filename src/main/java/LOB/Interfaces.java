package LOB;

import java.util.Comparator;

public class Interfaces { //Namespace
    
/**
 * An interface that expose getPrice() as well as giving a static Comparator 
 * member.
 * It does not enforce implementation of price setter, however.
 */
public static interface HasPrice {
    double getPrice();
    public static Comparator<HasPrice> PriceComparator = Comparator.comparingDouble(HasPrice::getPrice);
}

}
