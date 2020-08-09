package catboost.deserializer.jsondeserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by paras.mal on 9/8/20.
 */
public class CatHashDeserializer {

    public Map<String, Long> deserialize(JsonObject jsonObject) {
        JsonArray hashList = jsonObject.getAsJsonArray("cat_features_hash");
        if(hashList == null || hashList.isJsonNull()){
            return null;
        }
        Map<String, Long> hashes = new HashMap<String, Long>();
        for(JsonElement hashElement : hashList){
            JsonObject hashObj = hashElement.getAsJsonObject();
            hashes.put(hashObj.get("value").getAsString(), hashObj.get("hash").getAsLong());
        }
        return hashes;
    }
}
