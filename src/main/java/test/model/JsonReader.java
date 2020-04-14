package test.model;

import catboost.deserializer.jsondeserializer.JsonModelDeserializer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import test.Model;

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
                new File("/home/paras.mal/Documents/office/catboost/catboost/app/test.tsv")));
        //BufferedReader reader = new BufferedReader(new FileReader(new File("/tmp/in1.tsv")));
        List<Map<String,String>> list = new ArrayList<Map<String, String>>();

        String catf[] = {"device_id","os_id","browser_id","referer",
                "canonical_hash","mobile_model_cluster","location_cluster",
                "is_valid_visitor","seller_tag","hour_id","rc_num_calls"};
        String line = reader.readLine();
        int ii = 0;
        while(line != null && ii < 87000) {
            Map<String,String> map = new HashMap<String, String>();
            String input[] =line.split("\t");
                for (int i = 0; i < input.length; i++) {
                    map.put(catf[i], input[i]);
                }

            list.add(map);
            line = reader.readLine();
            ii++;
        }
        return list;
    }


    public JsonObject read(String file) throws Exception{
        BufferedReader br = new BufferedReader(
                new FileReader(file));


        JsonObject model = new Gson().fromJson(br, JsonObject.class);
        return model;
    }


    public static Map<Integer, Double> getResults() throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(
                new File("/home/paras.mal/Documents/office/catboost/catboost/app/pypred_test.tsv")));
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

    static Map<Integer, String> getFM(){
        String catf[] = {"hour_id","rc_num_calls","device_id","os_id","browser_id","referer",
                "canonical_hash","mobile_model_cluster","location_cluster",
                "is_valid_visitor","seller_tag"};
        Map<Integer, String> m = new HashMap<>();
        for(int i = 0;i<catf.length;i++){
            m.put(i,catf[i]);
        }
        return m;
    }


    public static void main(String s[]) throws Exception{
        Map<Integer, Double> r = getResults();
        System.out.println("DocId\tRawFormulaVal");
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/tmp/o.tsv")));
        JsonObject jsonObject = new JsonReader().read("/home/paras.mal/Documents/office/catboost/catboost/app/m2.json");
        Model model = Model.build(jsonObject);
        catboost.model.Model m = new JsonModelDeserializer().deserialize(jsonObject,getFM());
        int i = 0;
        List<Map<String,String>> ll = getInput();
        long t1 = System.currentTimeMillis();
        for(Map<String,String> map : ll) {
            if(i < 87000) {
                Double total = 0.0;
                /*for (TreeNode node : model.getRoots()) {
                    Double d = new EvaluateTree().evaluate(map, model, node);
                    total += d;
                }*/
              //  System.out.println("\n\n\n\n");
                double t = m.predict(map);
                total = t;
               // writer.write(i + "\t" + total + "\t" + t + "\n");
               // System.out.println(" " + i + " " + total + " " + t + " " + r.get(i));
                if ((Math.abs(r.get(i) - total) > .000001) || (Math.abs(r.get(i) - t) > .000001)) {
                    System.out.println("error in " + i + " " + total + " " + t + " " + r.get(i));
                }
                if ((i % 1000) == 0) {
                    System.out.println("i = " + i + " " + (System.currentTimeMillis() - t1));

                }
            }
            i++;
        }

        writer.flush();
        writer.close();

        System.out.println(model);
    }
}
