package catboost.condition;

import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class FloatCondition implements Condition {

    private final String featureName;
    private final double border;

    public FloatCondition(String featureName, double border){
        this.featureName = featureName;
        this.border = border;
    }

    @Override
    public boolean isLeft(Map<String, String> input) {
        double featureValue = Double.valueOf(input.get(featureName));
        if(featureValue >= border ){
            return true;
        }
        return false;
    }
}
