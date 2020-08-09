package catboost.hash;

import java.util.Map;

/**
 * Created by paras.mal on 10/4/20.
 */
public class HashCalculator {

    public static Long CalcHash(Long a, Long b) {
        Long MAGIC_MULT = 0x4906ba494954cb65l;
        return MAGIC_MULT * (a + MAGIC_MULT * b);
    }

    public static long CalcCatFeatureHash(String s, Map<String, Long> hashes, Long notPresent){
        if(hashes != null){
            if(hashes.containsKey(s)){
                return hashes.get(s);
            }
            return notPresent;
        }
        return new CityHash().cityHash64(s) & 0xffffffffl;
    }

}
