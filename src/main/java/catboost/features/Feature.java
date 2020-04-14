package catboost.features;

import catboost.hash.CityHash;
import java.util.List;
import java.util.Map;

/**
 * Created by paras.mal on 12/4/20.
 */
public class Feature {
    private final List<FeatureComponent> featureComponents;
    private final String type;

    public Feature(List<FeatureComponent> featureComponents, String type){
        this.featureComponents = featureComponents;
        this.type = type;
    }

    public String getHash(Map<String,String> input){
        long hash = 0;
        for(FeatureComponent featureComponent : featureComponents){
            hash = featureComponent.getKey(hash, input);
        }
        return CityHash.toBigInteger(hash).toString();
    }

    @Override
    public int hashCode() {
        return  (type + featureComponents.stream().mapToLong(a -> a.hashCode()).sum()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Feature)){
            return false;
        }
        Feature f = (Feature)obj;
        if(!type.equals(f.type)){
            return false;
        }
        if(f.featureComponents.size() != featureComponents.size()){
            return false;
        }
        for(int i = 0;i<featureComponents.size();i++){
            if(!featureComponents.get(i).equals(f.featureComponents.get(i))){
                return false;
            }
        }
        return true;
    }
}
