package model.tree;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * Created by paras.mal on 11/4/20.
 */
public class BorderDetails {

    FeatureKey featureKeys;
    int findex = -1;
    double border;
    int index;
    String type;
    double priorNumberator;
    double shift;
    double priorDenomenator;
    int targetBorderIdx;
    double scale;

    static public Map<Integer,BorderDetails> build(JsonObject jsonObject){
        int index = 0;
        int findex = 0;
        Map<Integer, BorderDetails> borderDetailses = new HashMap<Integer, BorderDetails>();
        /*----------------------float features----------------------------------------*/
        JsonArray array = jsonObject.getAsJsonArray("float_features");
        for(int i = 0;i<array.size();i++){
            JsonObject jsonObject1 = array.get(i).getAsJsonObject();
            JsonArray borders = jsonObject1.getAsJsonArray("borders");
            FeatureKey key = new FeatureKey();
            key.type = "float_feature";
            key.identifier = new ArrayList<FeatureIndex>();
            FeatureIndex fi = new FeatureIndex();
            fi.flatFeatureIndex = jsonObject1.get("flat_feature_index").getAsInt();
            fi.floatFeatureIndex = jsonObject1.get("feature_index").getAsInt();
            key.identifier.add(fi);
            for(int j = 0;j<borders.size();j++){
                double d = borders.get(j).getAsDouble();
                BorderDetails borderDetails = new BorderDetails();
                borderDetails.featureKeys = key;
                borderDetails.border = d;
                borderDetails.type = "float_feature";
                borderDetails.index = index;
                borderDetails.findex = findex;
                //System.out.println(" index " + index + " " + findex);
                borderDetailses.put(index, borderDetails);
                index++;
            }
            findex++;
        }
        /*-----------------------------OneHotEncoding------------------------------------*/
        array = jsonObject.getAsJsonArray("categorical_features");
        for(int i = 0;i<array.size();i++){
            JsonObject jsonObject1 = array.get(i).getAsJsonObject();
            JsonArray borders = jsonObject1.getAsJsonArray("values");
            //if(borders == null)
            FeatureKey key = new FeatureKey();
            FeatureIndex fi = new FeatureIndex();
            fi.flatFeatureIndex = jsonObject1.get("flat_feature_index").getAsInt();
            fi.catFeatureIndex = jsonObject1.get("feature_index").getAsInt();
            key.identifier = new ArrayList<FeatureIndex>();
            key.identifier.add(fi);
            for(int j = 0;borders !=null && j<borders.size();j++){
                double d = borders.get(j).getAsDouble();
                BorderDetails borderDetails = new BorderDetails();
                borderDetails.featureKeys = key;
                borderDetails.border = d;
                borderDetails.type = "one_hot";
                borderDetails.index = index;
                borderDetails.findex = findex;
                //System.out.println(" index " + index + " " + findex);
                borderDetailses.put(index, borderDetails);
                index++;
            }
            if(borders!=null) {
                findex++;
            }
        }

        /*-----------------------------ctrs----------------------------------------------*/
        array = jsonObject.getAsJsonArray("ctrs");
        for(int i = 0;i<array.size();i++){
            if(findex == 157){
                System.out.println();
            }
            JsonObject jsonObject1 = array.get(i).getAsJsonObject();
            if(jsonObject1.getAsJsonArray("elements").size() == 3){
                //System.out.println("element " + findex + " " + i);
            }
            JsonArray borders = jsonObject1.getAsJsonArray("borders");
            FeatureKey key = FeatureKey.build(jsonObject1.get("identifier").getAsString());
            for(int j = 0;j<borders.size();j++){
                double d = borders.get(j).getAsDouble();
                BorderDetails borderDetails = new BorderDetails();
                borderDetails.featureKeys = key;
                borderDetails.border = d;
                borderDetails.type = "ctrs";
                borderDetails.index = index;
                borderDetails.findex = findex;
                //System.out.println(" index " + index + " " + findex);
                borderDetails.priorNumberator = jsonObject1.get("prior_numerator").getAsDouble();
                borderDetails.priorDenomenator = jsonObject1.get("prior_denomerator").getAsDouble();
                borderDetails.scale = jsonObject1.get("scale").getAsDouble();
                borderDetails.shift = jsonObject1.get("shift").getAsDouble();
                borderDetails.targetBorderIdx = jsonObject1.get("target_border_idx").getAsInt();
                borderDetailses.put(index, borderDetails);
                index++;
            }
            findex++;


        }
        return borderDetailses;
    }


}
