package catboost.condition;

import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public interface Condition {

    boolean isLeft(Map<String,String> input);

}
