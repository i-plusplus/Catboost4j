package model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.tree.FeatureKey;
import model.tree.FeatureValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by paras.mal on 10/4/20.
 */
public class CtrData {

    static public Map<FeatureKey, FeatureValue> build(JsonObject jsonObject){
        Map<FeatureKey, FeatureValue> ctrData = new HashMap<FeatureKey, FeatureValue>();
        for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()){
            ctrData.put(FeatureKey.build(entry.getKey()), FeatureValue.build(entry.getValue().getAsJsonObject()));
        }
        return ctrData;
    }

}
