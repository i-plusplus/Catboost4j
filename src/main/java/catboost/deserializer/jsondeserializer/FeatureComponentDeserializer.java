package catboost.deserializer.jsondeserializer;

import catboost.features.CategoricalFeatureComponent;
import catboost.features.FeatureComponent;
import catboost.features.FloatFeatureComponent;
import catboost.features.OneHotFeatureComponent;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class FeatureComponentDeserializer {

    FeatureComponent deserialize(JsonObject jsonObject, int numberOfFloatFeatures, Map<Integer, String> featureNames, Map<String, Long> hashes, Long hashNotPresent){
        if(jsonObject.get("cat_feature_index") != null
                && jsonObject.get("combination_element") != null
                && jsonObject.get("combination_element").getAsString().equals("cat_feature_exact_value")){
            return new OneHotFeatureComponent(featureNames.get(numberOfFloatFeatures + jsonObject.get("cat_feature_index").getAsInt()), jsonObject.get("value").getAsInt(), hashes, hashNotPresent);
        }else if(jsonObject.get("float_feature_index") != null){
            return new FloatFeatureComponent(featureNames.get(jsonObject.get("float_feature_index").getAsInt()),
                    jsonObject.get("border").getAsDouble());
        }else{
            return new CategoricalFeatureComponent(featureNames.get(numberOfFloatFeatures + jsonObject.get("cat_feature_index").getAsInt()), hashes, hashNotPresent);
        }
    }
}
