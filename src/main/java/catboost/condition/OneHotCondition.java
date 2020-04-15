package catboost.condition;

import catboost.hash.HashCalculator;

import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class OneHotCondition implements Condition {

    private final String featureName;
    private final long value;

    public OneHotCondition(String featureName, long value){
        this.featureName = featureName;
        this.value = value;
    }

    @Override
    public boolean isLeft(Map<String, String> input) {
        String featureValue = input.get(featureName);
        int hash = (int) HashCalculator.CalcCatFeatureHash(featureValue);
        if(hash == value){
            return true;
        }
        return false;
    }

}
