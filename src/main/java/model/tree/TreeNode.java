package model.tree;

/**
 * Created by paras.mal on 11/4/20.
 */
public class TreeNode {
    boolean isLeaf = false;
    TreeNode left, right;
    double leafValue;
    String splitType;
    int leafIndex;
    BorderDetails borderDetails;
}
