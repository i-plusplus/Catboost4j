package catboost.features;

import catboost.hash.HashCalculator;

import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class FloatFeatureComponent implements FeatureComponent {

    private final String featureName;
    private final double border;

    public FloatFeatureComponent(String featureName, double border){
        this.featureName = featureName;
        this.border = border;
    }

    @Override
    public long getKey(long old, Map<String, String> input) {
        double featureValue = Double.valueOf(input.get(featureName));
        int value = 0;
        if(featureValue > border){
            value = 1;
        }
        return HashCalculator.CalcHash(old, (long) (int) value);
    }

    @Override
    public int hashCode() {
        return String.valueOf(border).hashCode() + featureName.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FloatFeatureComponent)){
            return false;
        }
        FloatFeatureComponent featureComponent = (FloatFeatureComponent) obj;
        return featureComponent.featureName.equals(featureName)
                && (Double.compare(featureComponent.border, border)==0);
    }
}
