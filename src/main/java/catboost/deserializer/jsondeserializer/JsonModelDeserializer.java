package catboost.deserializer.jsondeserializer;

import catboost.beans.CategoricalStats;
import catboost.condition.Condition;
import catboost.features.Feature;
import catboost.model.Model;
import catboost.tree.TreeNode;
import com.google.gson.JsonObject;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class JsonModelDeserializer {


    public Model deserialize(JsonObject jsonModel, Map<Integer, String> featureNames){
        return deserialize(jsonModel, featureNames, -1l);
    }

    public Model deserialize(JsonObject jsonModel, Map<Integer, String> featureNames, Long hashNotPresent){
        int numberOfNumericalFeatures = jsonModel.getAsJsonObject("features_info").getAsJsonArray("float_features").size();
        Map<String, Long> hashes = new CatHashDeserializer().deserialize(jsonModel.getAsJsonObject("features_info"));
        Map<Feature, Map<String, CategoricalStats>> map = new CTRDataDeserializer().deserilize(jsonModel.getAsJsonObject("ctr_data"), numberOfNumericalFeatures, featureNames, hashes, hashNotPresent);
        Map<Integer, Condition> conditionMap = new ConditionSerializer().serialize(jsonModel.getAsJsonObject("features_info"), jsonModel.getAsJsonArray("oblivious_trees"), featureNames,map, hashes, hashNotPresent);
        List<TreeNode> nodes = new TreeSerializer().deserialize(jsonModel.getAsJsonArray("oblivious_trees"), conditionMap);
        Pair<Double,Double> scaleAndBias = new ScaleBiasDesieralizer().deserialize(jsonModel);
        return new Model(nodes, scaleAndBias.getKey(), scaleAndBias.getValue());


    }

    public Model deserialize(JsonObject jsonModel){
        return deserialize(jsonModel, new FeatureNamesDeserialzier().deserializer(jsonModel.getAsJsonObject("features_info")));
    }

    public Model deserialize(JsonObject jsonModel, List<String> floatFeatureNames, List<String> categoricalFeatureNames){
        Map<Integer, String> featureNames = new HashMap<>();
        int i = 0;
        for(;i<floatFeatureNames.size();i++){
            featureNames.put(i, floatFeatureNames.get(i));
        }
        int j = 0;
        for(;j<categoricalFeatureNames.size();i++){
            featureNames.put(i+j,categoricalFeatureNames.get(j));
        }
        return deserialize(jsonModel, featureNames);

    }


}
