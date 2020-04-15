package catboost.features;

import catboost.hash.HashCalculator;

import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class CategoricalFeatureComponent implements FeatureComponent {

    private final String featureName;

    public CategoricalFeatureComponent(String featureName){
        this.featureName = featureName;
    }

    @Override
    public long getKey(long old, Map<String, String> input) {
        String featureValue = input.get(featureName);
        return HashCalculator.CalcHash(old, (long) (int) HashCalculator.CalcCatFeatureHash(featureValue));
    }

    @Override
    public int hashCode() {
        return featureName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CategoricalFeatureComponent)){
            return false;
        }
        return featureName.equals(((CategoricalFeatureComponent) obj).featureName);
    }
}
