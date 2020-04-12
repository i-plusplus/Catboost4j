package model.tree;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by paras.mal on 10/4/20.
 */
public class FeatureValue {

    double counterDenominator;
    int hashStride;
    Map<String, Border> features = new HashMap<String, Border>();


    public static FeatureValue build(JsonElement jsonElement){
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        FeatureValue fv = new FeatureValue();
        if(jsonObject.has("counter_denominator")){
            fv.counterDenominator = jsonObject.get("counter_denominator").getAsDouble();
        }
        if(jsonObject.has("hash_stride")){
            fv.hashStride = jsonObject.get("hash_stride").getAsInt();
        }
        JsonArray jsonArray = jsonObject.getAsJsonArray("hash_map");

        for(int i = 0;i<jsonArray.size();i+=fv.hashStride){
            String hash = jsonArray.get(i).getAsString();
            Border border = null;
            if(fv.hashStride == 3){
                border = new Border(jsonArray.get(i+2).getAsInt(), jsonArray.get(i+1).getAsInt(), i/3);
            }else {
                border = new Border(jsonArray.get(i+1).getAsInt(),-1,i/3 );
            }
            fv.features.put(hash, border);
        }
        return fv;
    }
}
