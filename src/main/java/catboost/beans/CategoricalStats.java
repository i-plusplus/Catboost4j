package catboost.beans;

/**
 * Created by paras.mal on 14/4/20.
 */
public class CategoricalStats {

    private final double denominator;
    private final double numerator;
    private final int location;

    public double getDenominator() {
        return denominator;
    }

    public double getNumerator() {
        return numerator;
    }

    public int getLocation() {
        return location;
    }


    public CategoricalStats(double denominator, double numerator, int location){
        this.denominator = denominator;
        this.numerator = numerator;
        this.location = location;
    }

}
