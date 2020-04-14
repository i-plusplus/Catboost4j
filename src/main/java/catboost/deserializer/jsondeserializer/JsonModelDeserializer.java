package catboost.deserializer.jsondeserializer;

import catboost.beans.CategoricalStats;
import catboost.condition.Condition;
import catboost.features.Feature;
import catboost.model.Model;
import catboost.tree.TreeNode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class JsonModelDeserializer {

    public Model deserialize(JsonObject jsonModel, Map<Integer, String> featureNames){
        int numberOfNumericalFeatures = jsonModel.getAsJsonObject("features_info").getAsJsonArray("float_features").size();
        Map<Feature, Map<String, CategoricalStats>> map = new CTRDataDeserializer().deserilize(jsonModel.getAsJsonObject("ctr_data"), numberOfNumericalFeatures, featureNames);
        Map<Integer, Condition> conditionMap = new ConditionSerializer().serialize(jsonModel.getAsJsonObject("features_info"),featureNames,map);
        List<TreeNode> nodes = new TreeSerializer().deserialize(jsonModel.getAsJsonArray("oblivious_trees"), conditionMap);
        return new Model(nodes);

    }

}
