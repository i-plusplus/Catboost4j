package catboost.deserializer.jsondeserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by paras.mal on 9/8/20.
 */
public class FeatureNamesDeserialzier {

    public Map<Integer, String> deserializer(JsonObject features){
        Map<Integer, String> featureNames = new HashMap<>();
        JsonArray array = features.getAsJsonArray("float_features");
        int index = 0;
        if(array != null && !array.isJsonNull()) {
            for (int i = 0; i < array.size(); i++) {
                JsonObject jsonObject1 = array.get(i).getAsJsonObject();
                JsonElement featureName = jsonObject1.get("feature_name");
                if(featureName == null || featureName.isJsonNull()){
                    throw new RuntimeException("feature_name is missing in float features, either save the model with pool or pass feature_name to index map");
                }
                featureNames.put(index, featureName.getAsString());
                index++;

            }
        }
        array = features.getAsJsonArray("categorical_features");
        if(array != null && !array.isJsonNull()) {
            for (int i = 0; i < array.size(); i++) {
                JsonObject jsonObject1 = array.get(i).getAsJsonObject();
                JsonElement featureName = jsonObject1.get("feature_name");
                if(featureName == null || featureName.isJsonNull()){
                    throw new RuntimeException("feature_name is missing in cat features, either save the model with pool or pass feature_name to index map");
                }
                featureNames.put(index, featureName.getAsString());
                index++;

            }
        }
        return featureNames;
    }
}
