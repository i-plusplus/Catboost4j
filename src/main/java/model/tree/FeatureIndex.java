package model.tree;

import com.google.gson.annotations.SerializedName;

/**
 * Created by paras.mal on 10/4/20.
 */
public class FeatureIndex {
    @SerializedName(value = "value")
    int value;
    @SerializedName(value = "cat_feature_index")
    int catFeatureIndex = -1;
    @SerializedName(value = "combination_element")
    String combinationElement ;
    @SerializedName(value = "float_feature_index")
    int floatFeatureIndex = -1;
    @SerializedName(value = "border")
    double border = -1.0;
    int flatFeatureIndex = -1;
    @Override
    public boolean equals(Object obj) {
        FeatureIndex fi = (FeatureIndex)obj;
        return catFeatureIndex == fi.catFeatureIndex
                && combinationElement.equals(fi.combinationElement)
                && floatFeatureIndex == fi.floatFeatureIndex
                && Double.compare(border ,fi.border) == 0
                && flatFeatureIndex == fi.flatFeatureIndex
                && value == fi.value;
    }




}
