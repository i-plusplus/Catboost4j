package catboost.model;

import catboost.tree.TreeNode;

import java.util.List;
import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
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

    public double predict(Map<String, String> input){
        double result = 0.0;
        for(TreeNode root : roots){
            result += root.compute(input);
        }
        return result*scale + bias;
    }
    public double predict(Map<String, String> input, int startTree, int endTree){
        double result = 0.0;
        for(int i = startTree;i<endTree;i++){
            TreeNode root = roots.get(i);
            result += root.compute(input);
        }
        return result*scale + bias;
    }

}
