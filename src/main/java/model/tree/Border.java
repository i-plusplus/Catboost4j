package model.tree;

/**
 * Created by paras.mal on 10/4/20.
 */
public class Border {

    private double success;
    private double failures;
    private int location;
    public Border(double success, double failures, int location){
        this.success = success;
        this.failures = failures;
        this.location = location;
    }

    public double getFailures() {
        return failures;
    }

    public double getSuccess() {
        return success;
    }



    public int getLocation() {
        return location;
    }
}
