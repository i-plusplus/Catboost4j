package model.tree;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by paras.mal on 10/4/20.
 */
public class FeatureKey {
    @SerializedName(value = "identifier")
    List<FeatureIndex> identifier;
    @SerializedName(value = "type")
    String type;

    @Override
    public boolean equals(Object obj) {
        FeatureKey fk = (FeatureKey)obj;
        if(identifier.size() != fk.identifier.size()){
            return false;
        }
        for(int i = 0;i<identifier.size();i++){
            if(!identifier.get(i).equals(fk.identifier.get(i))){
                return false;
            }
        }
        return type.equals(fk.type);
    }

    @Override
    public int hashCode() {
        return 1;
    }


    static public FeatureKey build(String s){
        return new Gson().fromJson(s, FeatureKey.class);
    }

    public static void main(String s[]){
        FeatureKey fk = build("{\"identifier\":[{\"cat_feature_index\":5,\"combination_element\":\"cat_feature_value\"},{\"cat_feature_index\":6,\"combination_element\":\"cat_feature_value\"}],\"type\":\"Borders\"}");
        System.out.println(fk);
    }

}
