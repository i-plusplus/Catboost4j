package catboost.condition;

import catboost.beans.CategoricalStats;
import catboost.features.Feature;

import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
abstract public class CategoricalCondition implements Condition {

    private final Feature feature;
    private final Map<String, CategoricalStats> stats;
    private final double priorNumerator;
    private final double priorDenominator;
    private final double scale;
    private final double shift;
    private final double border;

    public CategoricalCondition(Feature feature,
                                Map<String, CategoricalStats> stats,
                                double priorNumerator,
                                double priorDenominator,
                                double scale,
                                double shift,
                                double border){
        this.feature = feature;
        this.stats = stats;
        this.priorNumerator = priorNumerator;
        this.priorDenominator = priorDenominator;
        this.scale = scale;
        this.shift = shift;
        this.border = border;
    }

    public double getPriorDenominator() {
        return priorDenominator;
    }

    public double getPriorNumerator() {
        return priorNumerator;
    }

    public double getScale() {
        return scale;
    }

    public double getShift() {
        return shift;
    }

    public double getBorder() {
        return border;
    }

    abstract public double getNumerator(CategoricalStats categoricalStats);

    abstract public double getDenominator(CategoricalStats categoricalStats);


    @Override
    public boolean isLeft(Map<String, String> input) {
        String hashValue = feature.getHash(input);

        CategoricalStats categoricalStats = stats.get(hashValue);
        if(categoricalStats == null){
            categoricalStats = new CategoricalStats(0,0,-1);
        }
        double denominator = getDenominator(categoricalStats), numenator = getNumerator(categoricalStats);

        double nodeValue = ((numenator/denominator) + getShift()) * getScale();

        if(nodeValue >= getBorder()){
            return true;
        }
        return false;

    }
}
