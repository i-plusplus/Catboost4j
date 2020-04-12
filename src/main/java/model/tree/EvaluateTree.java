package model.tree;

import model.Model;
import model.tree.TreeNode;

import java.util.Map;

/**
 * Created by paras.mal on 11/4/20.
 */
public class EvaluateTree {

    public double evaluate(Map<String, String> input, Model model, TreeNode root){

        if(root.isLeaf){
            //System.out.println("leaf value = " + root.leafValue + " leafIndex = " + root.leafIndex);
            return root.leafValue;

        }
        if(new FeatureValueComputation().computeValue(input, model, root)){
            //System.out.println("right ");
            return evaluate(input, model, root.right);
        }else{
            //System.out.println("left ");
            return evaluate(input, model, root.left);
        }
    }

}
