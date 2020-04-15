package catboost.model;

import catboost.tree.TreeNode;

import java.util.List;
import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class Model {

    private final List<TreeNode> roots;

    public Model(List<TreeNode> roots){
        this.roots = roots;
    }

    public double predict(Map<String, String> input){
        double result = 0.0;
        for(TreeNode root : roots){
            result += root.compute(input);
        }
        return result;
    }

}
