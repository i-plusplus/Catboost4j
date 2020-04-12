package model.tree;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by paras.mal on 11/4/20.
 */
public class OblivionTree {



    static public List<TreeNode> build(JsonArray jsonArray, Map<Integer, BorderDetails> detailsMap){
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        for(int i = 0;i<jsonArray.size();i++){
            nodes.add(build(jsonArray.get(i).getAsJsonObject(), 0, 0, detailsMap,i));

        }
        return nodes;

    }




    static public TreeNode build(JsonObject jsonObject, int i, int nodeLocation, Map<Integer, BorderDetails> detailsMap, int tn) {


        JsonArray array = null;
        if (!jsonObject.has("splits") || new JsonNull().equals(jsonObject.get("splits"))) {
            array = new JsonArray();
        } else{
            array = jsonObject.getAsJsonArray("splits");
        }
        TreeNode root = new TreeNode();
        if(array.size() == i){
            root.isLeaf = true;
            root.leafValue = jsonObject.getAsJsonArray("leaf_values").get(nodeLocation).getAsDouble();
            root.leafIndex = nodeLocation;
        }else{
            root.borderDetails = detailsMap.get(array.get(array.size()-1-i).getAsJsonObject().get("split_index").getAsInt());

            root.splitType = array.get(array.size()-1-i).getAsJsonObject().get("split_type").getAsString();
            //root.left = build(jsonObject, i+1, (nodeLocation << 1)+1, detailsMap);
            //root.right = build(jsonObject, i+1,(nodeLocation << 1), detailsMap);
            root.left = build(jsonObject, i+1, nodeLocation << 1, detailsMap,tn);
            root.right = build(jsonObject, i+1,(nodeLocation << 1) + 1, detailsMap,tn);
        }

        return root;
    }





}
