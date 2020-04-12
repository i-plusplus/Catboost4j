package hash;

import javafx.util.Pair;
import sun.misc.Unsafe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Created by paras.mal on 9/4/20.
 */
public class CityHash {
    static  long k0 = 0xc3a5c85c97cb3127l;
    static  long k1 = 0xb492b66fbe98f273l;
    static  long k2 = 0x9ae16a3b2f90404fl;
    static  long k3 = 0xc949d7c7509e6557l;

    static Long UNALIGNED_LOAD64(String s){
        byte b[] = s.getBytes();
        long k = (short)b[0];
        for(int i = 1;i < 8;i++){
            k = k + ((long)b[i] << (8*i));
        }

        return k;
    }


    static  Integer UNALIGNED_LOAD32(String s){
        byte b[] = s.getBytes();
        int k = (short)b[0];
        for(int i = 1;i < 4;i++){
            k = k + ((int)b[i] << (8*i));
        }

        return k;
    }
    public static void main(String s[]) throws Exception{
        long l = new CityHash().CityHash64("0");
        System.out.println("bla bla " + toBigInteger(l).toString()
                + " " + (l & 0xffffffffl ));
       // Long l2 = new CityHash().CityHash64("002eb78051613e5da389e15af95a4356.d2s-002eb78051613e5da389e15af95a4356.d2s-002eb78051613e5da389e15af95a4356.d2s");
       // Long l3 = Hash128to64(new Pair<Long, Long>(l,l2));
       // System.out.println(toBigInteger(l).toString() + " " + toBigInteger(l2).toString() + " " + toBigInteger(l3).toString());
    }


    static long Uint128Low64(Pair<Long,Long> x){
        return x.getKey();
    }
    static long Uint128High64(Pair<Long,Long> x){
        return x.getValue();
    }
    private static final BigInteger BI_2_64 = BigInteger.ONE.shiftLeft(64);

    public static String asString(long l) {
        return l >= 0 ? String.valueOf(l) : toBigInteger(l).toString();
    }

    public static BigInteger toBigInteger(long l) {
        final BigInteger bi = BigInteger.valueOf(l);
        return l >= 0 ? bi : bi.add(BI_2_64);
    }
    static Long Hash128to64(Pair<Long,Long> x) {
        // Murmur-inspired hashing.
        long kMul = 0x9ddfea08eb382d69l;
        long a = (Uint128Low64(x) ^ Uint128High64(x)) * kMul;
        //System.out.println(a);
        a ^= (a >>> 47);
       // System.out.println(a);
        long b = (Uint128High64(x) ^ a) * kMul;
        //System.out.println();
        b ^= (b >>> 47);
        b *= kMul;
        return b;
    }


    static long HashLen16(long u, long v) {
        return Hash128to64(new Pair<Long, Long>(u, v));
    }

    static long RotateByAtLeast1(long val, int shift) {
        return (val >>> shift) | (val << (64 - shift));
    }

    static long ShiftMix(long val) {
        return val ^ (val >>> 47);
    }

    Long HashLen0to16(String s) {
        if (s.length() > 8) {
            Long a = UNALIGNED_LOAD64(s);
            Long b = UNALIGNED_LOAD64(s.substring(s.length() - 8));
            return HashLen16(a, RotateByAtLeast1(b + s.length(), s.length())) ^ b;
        }
        if (s.length() >= 4) {
            Long a = new Long(UNALIGNED_LOAD32(s));
            return HashLen16(s.length() + (a << 3), UNALIGNED_LOAD32(s.substring(s.length() - 4)));
        }
        if (s.length() > 0) {
            short a = (short)s.charAt(0);
            short b = (short)s.charAt(s.length() >> 1);
            short c = (short)s.charAt(s.length() - 1);
            int y = (int)(a) + ((int)(b) << 8);
            int z =s.length() + ((int)(c) << 2);
            return ShiftMix(y * k2 ^ z * k3) * k2;
        }
        return k2;
    }

    static long Rotate(long val, int shift) {
        // Avoid shifting by 64: doing so yields an undefined result.
        return shift == 0 ? val : ((val >>> shift) | (val << (64 - shift)));
    }

    static long HashLen17to32(String s) {
        long a = UNALIGNED_LOAD64(s) * k1;
        long b = UNALIGNED_LOAD64(s.substring(8));
        long c = UNALIGNED_LOAD64(s.substring(s.length() - 8)) * k2;
        long d = UNALIGNED_LOAD64(s.substring(s.length() - 16)) * k0;
        return HashLen16(Rotate(a - b, 43) + Rotate(c, 30) + d,
                a + Rotate(b ^ k3, 20) - c + s.length());
    }


    static long HashLen33to64(String s) {
        long z = UNALIGNED_LOAD64(s.substring(24));
        long a = UNALIGNED_LOAD64(s) + (s.length() + UNALIGNED_LOAD64(s.substring(s.length() - 16))) * k0;
        long b = Rotate(a + z, 52);
        long c = Rotate(a, 37);
        a += UNALIGNED_LOAD64(s.substring(8));
        c += Rotate(a, 7);
        a += UNALIGNED_LOAD64(s.substring(16));
        long vf = a + z;
        long vs = b + Rotate(a, 31) + c;
        a = UNALIGNED_LOAD64(s.substring(16)) + UNALIGNED_LOAD64(s.substring(s.length() - 32));
        z = UNALIGNED_LOAD64(s.substring(s.length() - 8));
        b = Rotate(a + z, 52);
        c = Rotate(a, 37);
        a += UNALIGNED_LOAD64(s.substring(s.length() - 24));
        c += Rotate(a, 7);
        a += UNALIGNED_LOAD64(s.substring(s.length() - 16));
        long wf = a + z;
        long ws = b + Rotate(a, 31) + c;
        long r = ShiftMix((vf + ws) * k2 + (wf + vs) * k0);
        return ShiftMix(r * k0 + vs) * k2;
    }

    static Pair<Long, Long> WeakHashLen32WithSeeds(
            long w, long x, long y, long z, long a, long b) {
        a += w;
        b = Rotate(b + a + z, 21);
        long c = a;
        a += x;
        a += y;
        b += Rotate(a, 44);
        return new Pair<Long,Long>(a + z, b + c);
    }


    static Pair<Long, Long> WeakHashLen32WithSeeds(
            String s, long a, long b) {
        return WeakHashLen32WithSeeds(UNALIGNED_LOAD64(s),
                UNALIGNED_LOAD64(s.substring(8)),
                UNALIGNED_LOAD64(s.substring(16)),
                UNALIGNED_LOAD64(s.substring(24)),
                a,
                b);
    }

    Pair<Long,Long> DoSwap(long a, long b){
        return new Pair<Long, Long>(b,a);
    }
    long CityHash64(String s) {
        if (s.length() <= 32) {
            if (s.length() <= 16) {
                return HashLen0to16(s);
            } else {
                return HashLen17to32(s);
            }
        } else if (s.length() <= 64) {
            return HashLen33to64(s);
        }

        // For strings over 64 bytes we hash the end first, and then as we
        // loop we keep 56 bytes of state: v, w, x, y, and z.
        long x = UNALIGNED_LOAD64(s);
        long y = UNALIGNED_LOAD64(s.substring(s.length() - 16)) ^ k1;
        long z = UNALIGNED_LOAD64(s.substring(s.length() - 56)) ^ k0;
        Pair<Long, Long> v = WeakHashLen32WithSeeds(s.substring(s.length() - 64), s.length(), y);
        Pair<Long, Long> w = WeakHashLen32WithSeeds(s.substring(s.length() - 32), s.length() * k1, k0);
        z += ShiftMix(v.getValue()) * k1;
        x = Rotate(z + x, 39) * k1;
        y = Rotate(y, 33) * k1;

        // Decrease len to the nearest multiple of 64, and operate on 64-byte chunks.
        int len = (s.length() - 1) & ~(63);
        do {
            x = Rotate(x + y + v.getKey() + UNALIGNED_LOAD64(s.substring(16)), 37) * k1;
            y = Rotate(y + v.getValue() + UNALIGNED_LOAD64(s.substring(48)), 42) * k1;
            x ^= w.getValue();
            y ^= v.getKey();
            z = Rotate(z ^ w.getKey(), 33);
            v = WeakHashLen32WithSeeds(s, v.getValue() * k1, x + w.getKey());
            w = WeakHashLen32WithSeeds(s.substring(32), z + w.getValue(), y);
            Pair<Long,Long> zx = DoSwap(z, x);
            z = zx.getKey();
            x = zx.getValue();
            s  = s.substring(64);
            len -= 64;
            /*System.out.println("print s " + s + " " + len
                    + " " + toBigInteger(x).toString()
                    + " " + toBigInteger(y).toString()
                    + " " + toBigInteger(z).toString()
            + " " + toBigInteger(v.getKey()).toString()
            + " " + toBigInteger(v.getValue()).toString()
            + " " + toBigInteger(w.getKey()).toString()
            + " " + toBigInteger(w.getValue()).toString());*/
        } while (len != 0);
        /*System.out.println("outside " + v.getKey() + " " + w.getKey() + " " + HashLen16(v.getKey(), w.getKey()) + " " + y + " "
                + toBigInteger(ShiftMix(y)).toString() + " " +
                toBigInteger(v.getValue()).toString() + " " + toBigInteger(w.getValue()).toString() + " " + toBigInteger(HashLen16(v.getValue(), w.getValue())).toString()
                + " " + toBigInteger(k1).toString() +
                " " + toBigInteger(z).toString() + " " + toBigInteger(HashLen16(HashLen16(v.getKey(), w.getKey()) + ShiftMix(y) * k1 + z,
                HashLen16(v.getValue(), w.getValue()) + x)).toString());*/

        return HashLen16(HashLen16(v.getKey(), w.getKey()) + ShiftMix(y) * k1 + z,
                HashLen16(v.getValue(), w.getValue()) + x);


/*
        len = (len - 1) & ~static_cast<size_t>(63);
        do {
            x = Rotate(x + y + v.first + UNALIGNED_LOAD64(s + 16), 37) * k1;
            y = Rotate(y + v.second + UNALIGNED_LOAD64(s + 48), 42) * k1;
            x ^= w.second;
            y ^= v.first;
            z = Rotate(z ^ w.first, 33);
            v = WeakHashLen32WithSeeds(s, v.second * k1, x + w.first);
            w = WeakHashLen32WithSeeds(s + 32, z + w.second, y);
            DoSwap(z, x);
            s += 64;
            len -= 64;
        } while (len != 0);
        return HashLen16(HashLen16(v.first, w.first) + ShiftMix(y) * k1 + z,
                HashLen16(v.second, w.second) + x);

  */  }



}
