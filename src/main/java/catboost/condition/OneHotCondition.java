package catboost.condition;

import catboost.hash.HashCalculator;

import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class OneHotCondition implements Condition {

    private final String featureName;
    private final long value;
    private final Map<String, Long> hashes;
    private final Long hashNotPresent;
    public OneHotCondition(String featureName, long value, Map<String, Long> hashes, Long hashNotPresent){
        this.featureName = featureName;
        this.value = value;
        this.hashes = hashes;
        this.hashNotPresent = hashNotPresent;
    }

    @Override
    public boolean isLeft(Map<String, String> input) {
        String featureValue = input.get(featureName);
        int hash = (int) HashCalculator.CalcCatFeatureHash(featureValue, hashes, hashNotPresent);
        if(hash == value){
            return true;
        }
        return false;
    }

}
