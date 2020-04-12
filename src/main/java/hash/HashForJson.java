package hash;

/**
 * Created by paras.mal on 10/4/20.
 */
public class HashForJson {

    public static Long CalcHash(Long a, Long b) {
        Long MAGIC_MULT = 0x4906ba494954cb65l;
       /* System.out.println("magic hashing3 "
                + CityHash.toBigInteger(a).toString()
                + " b " + CityHash.toBigInteger(b).toString()
                + " magic " + CityHash.toBigInteger(MAGIC_MULT).toString()
                + " result " + CityHash.toBigInteger(MAGIC_MULT * (a + MAGIC_MULT * b)).toString() );*/
        return MAGIC_MULT * (a + MAGIC_MULT * b);
    }

    public static long CalcCatFeatureHash(String s){
        return new CityHash().CityHash64(s) & 0xffffffffl;
    }




    public static void main(String s[]){
        long l = CalcHash(0l, (long) (int) CalcCatFeatureHash("002eb78051613e5da389e15af95a4356.d2s"));
        System.out.println(CityHash.toBigInteger(l).toString());
        //long k = (int)CalcCatFeatureHash("0");
        //System.out.println((int)k);

        //System.out.println((int) CalcCatFeatureHash("1"));
        //System.out.println((int) CalcCatFeatureHash("NAEMP"));

        l = CalcHash(l,(long)(int)CalcCatFeatureHash("0"));
        //l = CalcHash(l,0l);

        System.out.println("final " + CityHash.toBigInteger(l).toString());
//        k = CalcCatFeatureHash("0");
  //      System.out.println(k);
    //    l = CalcHash(l, (long) (int) 0);
      //  System.out.println(CityHash.toBigInteger(l).toString());



    }



}
