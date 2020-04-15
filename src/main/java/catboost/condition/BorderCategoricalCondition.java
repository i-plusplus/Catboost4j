package catboost.condition;

import catboost.beans.CategoricalStats;
import catboost.features.Feature;

import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class BorderCategoricalCondition extends CategoricalCondition {

    public BorderCategoricalCondition(Feature feature,
                                      Map<String, CategoricalStats> stats,
                                      double priorNumerator,
                                      double priorDenominator,
                                      double scale,
                                      double shift,
                                      double border){
        super(feature, stats, priorNumerator, priorDenominator, scale, shift, border);
    }

    public double getNumerator(CategoricalStats categoricalStats){
        return categoricalStats.getNumerator() + getPriorNumerator();
    }

    public double getDenominator(CategoricalStats categoricalStats){
        return categoricalStats.getDenominator() + categoricalStats.getNumerator() + getPriorDenominator();
    }


}
