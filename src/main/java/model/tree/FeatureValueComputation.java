package model.tree;

import hash.CityHash;
import hash.HashForJson;
import model.Model;
import model.tree.BorderDetails;
import model.tree.FeatureIndex;

import java.util.Map;

/**
 * Created by paras.mal on 11/4/20.
 */
public class FeatureValueComputation {


    public boolean computeValue(Map<String, String> input, Model model, TreeNode node ){
        long hash = 0;
        BorderDetails borderDetails = node.borderDetails;
        if(borderDetails == null) return true;
        String name= null;
        if(node.splitType.equals("FloatFeature")){
            name = model.getFeatureNames().get(borderDetails.featureKeys.identifier.get(0).floatFeatureIndex);
            double featureValue = Double.valueOf(input.get(name));
            //System.out.println(name + " " + featureValue + " " + borderDetails.border);
            if(featureValue >= borderDetails.border ){
                return true;
            }
            return false;
        }
        if(node.splitType.equals("OneHotFeature")){
            name = model.getFeatureNames().get(borderDetails.featureKeys.identifier.get(0).catFeatureIndex + model.getNumberOfFloatFeatures());
            String featureValue = input.get(name);
            int k = (int)HashForJson.CalcCatFeatureHash(featureValue);
            if(k == borderDetails.border){
                return true;
            }
            return false;

        }
        if(borderDetails== null || borderDetails.featureKeys == null || borderDetails.featureKeys.identifier == null){
            System.out.println();
        }
        for(FeatureIndex featureIndex : borderDetails.featureKeys.identifier){
            //System.out.println(CityHash.toBigInteger(hash).toString());
            if(featureIndex.catFeatureIndex != -1 && featureIndex.combinationElement.equals("cat_feature_exact_value")){
                name = model.getFeatureNames().get(featureIndex.catFeatureIndex+ model.getNumberOfFloatFeatures());
                String featureValue = input.get(name);
                int fvalue = (int)HashForJson.CalcCatFeatureHash(featureValue);
                int value = 0;
                if(fvalue == featureIndex.value){
                    value = 1;
                }
                hash = HashForJson.CalcHash(hash, (long) (int) value);
            }
             else if(featureIndex.catFeatureIndex != -1){
                name = model.getFeatureNames().get(featureIndex.catFeatureIndex+model.getNumberOfFloatFeatures());
                String featureValue = input.get(name);
                hash = HashForJson.CalcHash(hash, (long) (int) HashForJson.CalcCatFeatureHash(featureValue));
            }else{
                if(featureIndex.floatFeatureIndex != -1){
                    name = model.getFeatureNames().get(featureIndex.floatFeatureIndex);
                    double featureValue = Double.valueOf(input.get(name));
                    int value = 0;
                    if(featureValue > featureIndex.border){
                        value = 1;
                    }
                    hash = HashForJson.CalcHash(hash, (long) (int) value);
                }
            }

        }
        String hashValue = CityHash.toBigInteger(hash).toString();
        Border border = model.getCtrData().get(borderDetails.featureKeys).features.get(hashValue);
        if(border == null){
            border = new Border(0,0,-1);
        }
        double denominator = 0, numenator = 0;
        if(borderDetails.featureKeys.type.equals("Borders")){
            denominator = border.getFailures() + border.getSuccess() + borderDetails.priorDenomenator;
            numenator = border.getSuccess() + borderDetails.priorNumberator;
        }else{
            denominator = model.getCtrData().get(borderDetails.featureKeys).counterDenominator + borderDetails.priorDenomenator;
            numenator = border.getSuccess() + borderDetails.priorNumberator;
        }

        double nodeValue = ((numenator/denominator) + borderDetails.shift) * borderDetails.scale;
        //System.out.println(name + " hash=" + hashValue + " num=" + numenator + " deno="+denominator + " scale=" + borderDetails.scale + " ctr=" + nodeValue + " border=" + borderDetails.border);
        if(nodeValue >= borderDetails.border){
            return true;
        }
        return false;
    }
}
