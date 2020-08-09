package test.model;

import catboost.deserializer.jsondeserializer.JsonModelDeserializer;
import catboost.model.Model;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by paras.mal on 1/5/20.
 */
public class Test1Model {


    public static void main(String s[]) throws Exception{
        Model model = new JsonModelDeserializer().deserialize(read("/home/paras.mal/Downloads/ttm.json"));
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/home/paras.mal/Downloads/r.tsv"))));
        String rl = r.readLine();
        rl = r.readLine();
        List<Double> rs = new ArrayList<>();
        while(rl != null){
            rs.add(Double.parseDouble(rl));
            rl = r.readLine();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new
                File("/home/paras.mal/Downloads/td.tsv"))));
        String line = reader.readLine();
        String l2[] = line.split("\t");
        Map<Integer,String> f = new HashMap<>();
        for(int i = 0;i<l2.length;i++){
            f.put(i, l2[i]);
        }
        line = reader.readLine();
        int index = 0;
        while(line != null) {
            Map<String, String> map = new HashMap<>();
            String l[] = line.split("\t");
            for (int i = 0; i < l.length; i++) {
                map.put(f.get(i), l[i]);
            }
            double k = model.predict(map);
            System.out.println(k - rs.get(index));
            index++;
            line = reader.readLine();
        }

    }
    static public JsonObject read(String file) throws Exception{
        BufferedReader br = new BufferedReader(
                new FileReader(file));
        JsonObject model = new Gson().fromJson(br, JsonObject.class);
        return model;
    }



}
