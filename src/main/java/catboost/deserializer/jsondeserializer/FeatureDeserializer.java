package catboost.deserializer.jsondeserializer;


import catboost.features.Feature;
import catboost.features.FeatureComponent;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by paras.mal on 12/4/20.
 */
public class FeatureDeserializer  {
    public Feature deserialize(JsonObject json, int numberOfFloatFeatures, Map<Integer, String> featureNames) throws JsonParseException {
        List<FeatureComponent> featureComponents = new ArrayList<>();
        JsonArray identifier = json.getAsJsonArray("identifier");
        for(int i =0;i<identifier.size();i++){
            featureComponents.add(new FeatureComponentDeserializer().deserialize(identifier.get(i).getAsJsonObject(),numberOfFloatFeatures,featureNames));
        }
        return new Feature(featureComponents, json.get("type").getAsString());
    }

    public Feature deserialize(String json, int numberOfFloatFeatures, Map<Integer, String> featureNames) throws JsonParseException {
        return deserialize(new Gson().fromJson(json, JsonObject.class), numberOfFloatFeatures, featureNames);
    }

}
