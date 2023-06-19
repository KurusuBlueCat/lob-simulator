package src.main.java.LOB;

import java.util.Comparator;

public class Interfaces { //Namespace
    
public static interface HasPrice {
    void setPrice(double price);
    double getPrice();

    public static Comparator<HasPrice> PriceComparator = Comparator.comparingDouble(HasPrice::getPrice);
}

}
