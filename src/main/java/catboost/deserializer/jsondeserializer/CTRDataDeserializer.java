package catboost.deserializer.jsondeserializer;

import catboost.beans.CategoricalStats;
import catboost.features.Feature;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */




public class CTRDataDeserializer {

    public Map<Feature, Map<String,CategoricalStats>> deserilize(JsonObject jsonObject, int numberOfNumericalFeatures, Map<Integer, String> featureNames, Map<String, Long> hashes, Long hashNotPresent){
        Map<Feature, Map<String, CategoricalStats>> map = new HashMap<>();

        for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()){
            Feature key = new FeatureDeserializer().deserialize(entry.getKey(), numberOfNumericalFeatures, featureNames, hashes, hashNotPresent);
            Map<String,CategoricalStats> value = new HashMap<>();
            JsonObject obj = entry.getValue().getAsJsonObject();
            double counterDenominator = 0;
            if(obj.has("counter_denominator")){
                counterDenominator = obj.get("counter_denominator").getAsDouble();
            }
            int hashStride = 1;
            if(obj.has("hash_stride")){
                hashStride = obj.get("hash_stride").getAsInt();
            }
            JsonArray jsonArray = obj.getAsJsonArray("hash_map");
            for(int i = 0;i<jsonArray.size();i+=hashStride){
                String hash = jsonArray.get(i).getAsString();
                CategoricalStats categoricalStats = null;
                if(hashStride == 3){
                    categoricalStats = new CategoricalStats(jsonArray.get(i+1).getAsInt(), jsonArray.get(i+2).getAsInt(), i/3);
                }else {
                    categoricalStats = new CategoricalStats(counterDenominator,jsonArray.get(i+1).getAsInt(),i/3 );
                }
                value.put(hash, categoricalStats);
            }
            map.put(key, value);
        }
        return map;
    }





}
