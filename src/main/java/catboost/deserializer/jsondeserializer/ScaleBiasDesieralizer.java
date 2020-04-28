package catboost.deserializer.jsondeserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.util.Pair;


/**
 * Created by paras.mal on 28/4/20.
 */
public class ScaleBiasDesieralizer {

    public Pair<Double, Double> deserialize(JsonObject model){
        if(model.has("scale_and_bias")){
            JsonArray array = model.getAsJsonArray("scale_and_bias");
            return new Pair<Double,Double>(array.get(0).getAsDouble(), array.get(1).getAsDouble());
        }
        return new Pair<Double,Double>(1.0,0.0);
    }
}
