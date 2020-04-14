package catboost.tree;

import catboost.condition.Condition;

import java.util.Map;

/**
 * Created by paras.mal on 14/4/20.
 */
public class TreeNode {

    private final boolean isLeaf;
    private final double leafValue;
    private final int leafIndex;
    private final Condition condition;
    private final TreeNode left, right;

    public TreeNode(Condition condition, TreeNode left, TreeNode right){
        this.condition = condition;
        this.leafValue = 0.0;
        this.leafIndex = -1;
        this.isLeaf = false;
        this.left = left;
        this.right = right;
    }

    public TreeNode(double leafValue, int leafIndex){
        this.condition =null;
        this.left = null;
        this.right = null;
        this.isLeaf = true;
        this.leafValue = leafValue;
        this.leafIndex = leafIndex;
    }

    public double compute(Map<String,String> input) {
        if(isLeaf){
          //  System.out.println(leafIndex);
            return leafValue;
        }
        if(condition.isLeft(input)){
           // System.out.print("right ");
            return right.compute(input);
        }else{
           // System.out.print("left ");
            return left.compute(input);
        }
    }
}
