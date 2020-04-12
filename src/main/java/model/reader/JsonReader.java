package model.reader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.org.apache.xpath.internal.operations.Mod;
import model.Model;
import model.tree.*;
import model.CtrData;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paras.mal on 10/4/20.
 */
public class JsonReader {

    static public List<Map<String,String>> getInput() throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(
                new File("/home/paras.mal/Documents/office/catboost/catboost/app/in.tsv")));
        //BufferedReader reader = new BufferedReader(new FileReader(new File("/tmp/in1.tsv")));
        List<Map<String,String>> list = new ArrayList<Map<String, String>>();

        String catf[] = {"device_id","os_id","browser_id","referer",
                "canonical_hash","mobile_model_cluster","location_cluster",
                "is_valid_visitor","seller_tag","hour_id","rc_num_calls"};
        String line = reader.readLine();
        while(line != null) {
            Map<String,String> map = new HashMap<String, String>();
            String input[] =line.split("\t");
                for (int i = 0; i < input.length; i++) {
                    map.put(catf[i], input[i]);
                }
            list.add(map);
            line = reader.readLine();
        }
        return list;
    }


    public JsonObject read(String file) throws Exception{
        BufferedReader br = new BufferedReader(
                new FileReader(file));

        //convert the json string back to object
        JsonObject model = new Gson().fromJson(br, JsonObject.class);
        return model;
    }


    public static Map<Integer, Double> getResults() throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(
                new File("/home/paras.mal/Documents/office/catboost/catboost/app/pypred2.tsv")));
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        String line = reader.readLine();
        //line = reader.readLine();
        while(line != null){
            String token[] = line.split("\t");
            map.put(Integer.valueOf(token[0]), Double.valueOf(token[1]));
            line  = reader.readLine();
        }
        return map;
    }

    public static void main(String s[]) throws Exception{
        Map<Integer, Double> r = getResults();
        System.out.println("DocId\tRawFormulaVal");
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/tmp/o.tsv")));
        JsonObject jsonObject = new JsonReader().read("/tmp/m2.json");
        Model model = Model.build(jsonObject);
        int i = 0;
        for(Map<String,String> map : getInput()) {
            Double total = 0.0;
            for (TreeNode node : model.getRoots()) {
                Double d = new EvaluateTree().evaluate(map, model, node);
                total += d;
               // System.out.println("Tree Value = " + d);
            }
            //System.out.println(i + "\t" + total);
            writer.write(i + "\t" + total + "\n");
            if(Math.abs(r.get(i) - total) > .0001){
                System.out.println("error in " + i + " " + total + " " + r.get(i));
            }
            if((i %1000)==0){
                System.out.println("i = " + i);
            }

            i++;
        }
        writer.flush();
        writer.close();
        /*
        JsonObject ctrData = model.get("ctr_data").getAsJsonObject();
        Map<FeatureKey, FeatureValue> ctrDataMap = CtrData.build(ctrData);
        System.out.println(ctrDataMap);
        System.out.println(model.get("features_info"));
        Map<Integer, BorderDetails> map = BorderDetails.build(model.getAsJsonObject("features_info"));*/
        System.out.println(model);
    }
}
