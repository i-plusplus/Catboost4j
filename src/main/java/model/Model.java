package model;

import com.google.gson.JsonObject;
import model.tree.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paras.mal on 11/4/20.
 */
public class Model {
    Map<FeatureKey, FeatureValue> ctrData;
    List<TreeNode> roots;
    Map<Integer, String> featureNames = new HashMap<Integer, String>();
    ModelInfo modelInfo;
    Map<Integer, BorderDetails> borderDetailsMap;
    int numberOfFloatFeatures = 0;

    public int getNumberOfFloatFeatures() {
        return numberOfFloatFeatures;
    }

    public List<TreeNode> getRoots() {
        return roots;
    }

    public Map<FeatureKey, FeatureValue> getCtrData() {
        return ctrData;
    }

    public Map<Integer, BorderDetails> getBorderDetailsMap() {
        return borderDetailsMap;
    }

    public Map<Integer, String> getFeatureNames() {
        return featureNames;
    }

    public ModelInfo getModelInfo() {
        return modelInfo;
    }


    public static Model build(JsonObject jsonObject){
        Model model  = new Model();
        model.ctrData = CtrData.build(jsonObject.getAsJsonObject("ctr_data"));
        model.borderDetailsMap = BorderDetails.build(jsonObject.getAsJsonObject("features_info"));

        model.roots = OblivionTree.build(jsonObject.getAsJsonArray("oblivious_trees"), model.borderDetailsMap);
        model.modelInfo = ModelInfo.build(jsonObject.getAsJsonObject("model_info"));
        String catf[] = {"device_id","os_id","browser_id","referer","canonical_hash","mobile_model_cluster","location_cluster","is_valid_visitor","seller_tag"};
        String floatf[] = {"hour_id","rc_num_calls"};
        model.numberOfFloatFeatures = floatf.length;
        int i = 0;
        for(;i<floatf.length;i++){
            model.featureNames.put(i, floatf[i]);
        }

        for(;i<catf.length+floatf.length;i++){
            model.featureNames.put(i, catf[i-floatf.length]);
        }


        return model;
    }
}
