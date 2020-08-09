package catboost.model;

import catboost.tree.TreeNode;

import java.util.List;
import java.util.Map;

/**
 * This class is used for predicting the output of Catboost Model
 * @author parasmalik
 */
public class Model {

    private final List<TreeNode> roots;
    private final Double scale;
    private final Double bias;
    public Model(List<TreeNode> roots, Double scale, Double bias){
        this.roots = roots;
        this.scale = scale;
        this.bias  = bias;
    }

    /**
     * This is for calculating the prediction from catboost model.
     * @param input in this map key is the name of the feature as given while training the model and value is String.valueOf(valueOfFeature)
     * @return prediction of given model on given input
     */
    public double predict(Map<String, String> input){
        double result = 0.0;
        for(TreeNode root : roots){
            result += root.compute(input);
        }
        return result*scale + bias;
    }

    /**
     * This method is used for to compute only subset of trees instead of all trees.
     * @param input in this map key is the name of the feature as given while training the model and value is String.valueOf(valueOfFeature)
     * @param startTree starting(including) index of the tree from where you want to compute the trees. Index start from 0
     * @param endTree ending(excluding) index of the tree
     * @return prediction of given model on given input
     */
    public double predict(Map<String, String> input, int startTree, int endTree){
        double result = 0.0;
        for(int i = startTree;i<endTree;i++){
            TreeNode root = roots.get(i);
            result += root.compute(input);
        }
        return result*scale + bias;
    }

}
