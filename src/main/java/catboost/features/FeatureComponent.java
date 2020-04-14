package catboost.features;

import java.util.Map;

/**
 * Created by paras.mal on 12/4/20.
 */
public interface FeatureComponent {
    long getKey(long old, Map<String, String> input);
}
