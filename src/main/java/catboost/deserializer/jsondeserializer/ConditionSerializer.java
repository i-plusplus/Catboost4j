package catboost.deserializer.jsondeserializer;

import catboost.beans.CategoricalStats;
import catboost.condition.*;
import catboost.features.Feature;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class ConditionSerializer {

    Map<Integer, Condition> serialize(JsonObject jsonObject,
                                      Map<Integer, String> featureNames,
                                      Map<Feature, Map<String,
                                              CategoricalStats>> hashMap){
        int index = 0;
        int findex = 0;
        Map<Integer, Condition> conditionMap = new HashMap<>();
        int numberOfNumericalFeatures = jsonObject.getAsJsonArray("float_features").size();
        /*----------------------float features----------------------------------------*/
        JsonArray array = jsonObject.getAsJsonArray("float_features");
        for(int i = 0;i<array.size();i++){
            JsonObject jsonObject1 = array.get(i).getAsJsonObject();
            JsonArray borders = jsonObject1.getAsJsonArray("borders");
            String featureName = featureNames.get(i);
            for(int j = 0;j<borders.size();j++){
                conditionMap.put(index, new FloatCondition(featureName, borders.get(j).getAsDouble()));
                index++;
            }
            findex++;
        }
        /*-----------------------------OneHotEncoding------------------------------------*/
        array = jsonObject.getAsJsonArray("categorical_features");
        for(int i = 0;i<array.size();i++){
            JsonObject jsonObject1 = array.get(i).getAsJsonObject();
            JsonArray borders = jsonObject1.getAsJsonArray("values");
            int flatFeatureIndex = jsonObject1.get("flat_feature_index").getAsInt();
            String featureName = featureNames.get(flatFeatureIndex + numberOfNumericalFeatures);
            for(int j = 0;borders !=null && j<borders.size();j++){
                long d = borders.get(j).getAsLong();
                conditionMap.put(index, new OneHotCondition(featureName, d));
                index++;
            }
            if(borders!=null) {
                findex++;
            }
        }

        /*-----------------------------ctrs----------------------------------------------*/
        array = jsonObject.getAsJsonArray("ctrs");
        for(int i = 0;i<array.size();i++){

            JsonObject jsonObject1 = array.get(i).getAsJsonObject();

            JsonArray borders = jsonObject1.getAsJsonArray("borders");
            Feature key = new FeatureDeserializer().deserialize(
                    jsonObject1.get("identifier").getAsString(), numberOfNumericalFeatures, featureNames);
            Map<String, CategoricalStats> mp = hashMap.get(key);
            for(int j = 0;j<borders.size();j++){
                double d = borders.get(j).getAsDouble();

                double priorNumberator = jsonObject1.get("prior_numerator").getAsDouble();
                double priorDenomenator = jsonObject1.get("prior_denomerator").getAsDouble();
                double scale = jsonObject1.get("scale").getAsDouble();
                double shift = jsonObject1.get("shift").getAsDouble();
                String ctrType = jsonObject1.get("ctr_type").getAsString();
                if(ctrType.equals("Borders")){
                    conditionMap.put(index, new BorderCategoricalCondition(key, mp, priorNumberator,priorDenomenator,scale,shift,d));
                }else{
                    conditionMap.put(index, new CountCategoricalCondition(key, mp, priorNumberator,priorDenomenator,scale,shift,d));
                }

                index++;
            }
            findex++;


        }
        return conditionMap;



    }

}
