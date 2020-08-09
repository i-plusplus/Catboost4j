package catboost.features;

import catboost.hash.HashCalculator;

import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class OneHotFeatureComponent implements FeatureComponent {

    private final String featureName;
    private final int value;
    private final Map<String, Long> hashes;
    private final long hashNotPresent;
    public OneHotFeatureComponent(String featureName, int value, Map<String, Long> hashes, long hashNotPresent){
        this.featureName = featureName;
        this.value = value;
        this.hashes = hashes;
        this.hashNotPresent = hashNotPresent;
    }
    @Override
    public long getKey(long old, Map<String, String> input) {
        String featureValue = input.get(featureName);
        int fvalue = (int) HashCalculator.CalcCatFeatureHash(featureValue, hashes, hashNotPresent);
        int h = 0;
        if(fvalue == value){
            h = 1;
        }
        old = HashCalculator.CalcHash(old, (long) (int) h);
        return old;
    }

    @Override
    public int hashCode() {
        return value + featureName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof OneHotFeatureComponent)){
            return false;
        }
        OneHotFeatureComponent featureComponent = (OneHotFeatureComponent) obj;
        return featureComponent.featureName.equals(featureName)
                && (featureComponent.value == value);
    }
}
