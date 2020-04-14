package catboost.deserializer.jsondeserializer;

import catboost.condition.Condition;
import catboost.tree.TreeNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class TreeSerializer {

    public List<TreeNode> deserialize(JsonArray jsonArray, Map<Integer,Condition> conditionMap){

        List<TreeNode> nodes = new ArrayList<TreeNode>();
        for(int i = 0;i<jsonArray.size() && i<800;i++){
            nodes.add(deserialize(jsonArray.get(i).getAsJsonObject(), 0, 0, conditionMap, i));

        }
        return nodes;

    }

    public TreeNode deserialize(JsonObject jsonObject, int i, int nodeLocation, Map<Integer, Condition> conditionMap, int tn) {


        JsonArray array = null;
        if (!jsonObject.has("splits") || new JsonNull().equals(jsonObject.get("splits"))) {
            array = new JsonArray();
        } else{
            array = jsonObject.getAsJsonArray("splits");
        }

        if(array.size() == i){
            return new TreeNode(jsonObject.getAsJsonArray("leaf_values").get(nodeLocation).getAsDouble(), nodeLocation);
        }else{
            return new TreeNode(conditionMap.get(array.get(array.size()-1-i).getAsJsonObject().get("split_index").getAsInt()),
                    deserialize(jsonObject, i + 1, nodeLocation << 1, conditionMap, tn),
                    deserialize(jsonObject, i + 1, (nodeLocation << 1) + 1, conditionMap, tn));
        }

    }


}
