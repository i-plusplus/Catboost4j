package model;

import com.google.gson.JsonObject;

/**
 * Created by paras.mal on 10/4/20.
 */
public class ModelInfo {

    String ctrLeafCountLimit;

    public static ModelInfo build(JsonObject jsonObject){
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.ctrLeafCountLimit = jsonObject.getAsJsonObject("params").getAsJsonObject("cat_feature_params").get("ctr_leaf_count_limit").getAsString();
        return modelInfo;

    }
}
