package catboost.hash;

import javafx.util.Pair;

import java.math.BigInteger;

/**
 * Created by paras.mal on 9/4/20.
 * This complete class is blind translation from cpp code. I really don't understand how we are computing this catboost.hash.
 */
public class CityHash {

    static  long k0 = 0xc3a5c85c97cb3127l;
    static  long k1 = 0xb492b66fbe98f273l;
    static  long k2 = 0x9ae16a3b2f90404fl;
    static  long k3 = 0xc949d7c7509e6557l;

    static Long unalignedLOAD64(String s){
        byte b[] = s.getBytes();
        long k = (short)b[0];
        for(int i = 1;i < 8;i++){
            k = k + ((long)b[i] << (8*i));
        }

        return k;
    }


    static  Integer unalignedLOAD32(String s){
        byte b[] = s.getBytes();
        int k = (short)b[0];
        for(int i = 1;i < 4;i++){
            k = k + ((int)b[i] << (8*i));
        }

        return k;
    }

    static long uint128Low64(Pair<Long, Long> x){
        return x.getKey();
    }
    static long uint128High64(Pair<Long, Long> x){
        return x.getValue();
    }
    private static final BigInteger BI_2_64 = BigInteger.ONE.shiftLeft(64);


    public static BigInteger toBigInteger(long l) {
        final BigInteger bi = BigInteger.valueOf(l);
        return l >= 0 ? bi : bi.add(BI_2_64);
    }
    static private Long hash128to64(Pair<Long, Long> x) {
        long kMul = 0x9ddfea08eb382d69l;
        long a = (uint128Low64(x) ^ uint128High64(x)) * kMul;
        a ^= (a >>> 47);
        long b = (uint128High64(x) ^ a) * kMul;
        b ^= (b >>> 47);
        b *= kMul;
        return b;
    }


    static long hashLen16(long u, long v) {
        return hash128to64(new Pair<Long, Long>(u, v));
    }

    static long rotateByAtLeast1(long val, int shift) {
        return (val >>> shift) | (val << (64 - shift));
    }

    static long shiftMix(long val) {
        return val ^ (val >>> 47);
    }

    Long hashLen0to16(String s) {
        if (s.length() > 8) {
            Long a = unalignedLOAD64(s);
            Long b = unalignedLOAD64(s.substring(s.length() - 8));
            return hashLen16(a, rotateByAtLeast1(b + s.length(), s.length())) ^ b;
        }
        if (s.length() >= 4) {
            Long a = new Long(unalignedLOAD32(s));
            return hashLen16(s.length() + (a << 3), unalignedLOAD32(s.substring(s.length() - 4)));
        }
        if (s.length() > 0) {
            short a = (short)s.charAt(0);
            short b = (short)s.charAt(s.length() >> 1);
            short c = (short)s.charAt(s.length() - 1);
            int y = (int)(a) + ((int)(b) << 8);
            int z =s.length() + ((int)(c) << 2);
            return shiftMix(y * k2 ^ z * k3) * k2;
        }
        return k2;
    }

    static long rotate(long val, int shift) {
        return shift == 0 ? val : ((val >>> shift) | (val << (64 - shift)));
    }

    static long hashLen17to32(String s) {
        long a = unalignedLOAD64(s) * k1;
        long b = unalignedLOAD64(s.substring(8));
        long c = unalignedLOAD64(s.substring(s.length() - 8)) * k2;
        long d = unalignedLOAD64(s.substring(s.length() - 16)) * k0;
        return hashLen16(rotate(a - b, 43) + rotate(c, 30) + d,
                a + rotate(b ^ k3, 20) - c + s.length());
    }


    static long hashLen33to64(String s) {
        long z = unalignedLOAD64(s.substring(24));
        long a = unalignedLOAD64(s) + (s.length() + unalignedLOAD64(s.substring(s.length() - 16))) * k0;
        long b = rotate(a + z, 52);
        long c = rotate(a, 37);
        a += unalignedLOAD64(s.substring(8));
        c += rotate(a, 7);
        a += unalignedLOAD64(s.substring(16));
        long vf = a + z;
        long vs = b + rotate(a, 31) + c;
        a = unalignedLOAD64(s.substring(16)) + unalignedLOAD64(s.substring(s.length() - 32));
        z = unalignedLOAD64(s.substring(s.length() - 8));
        b = rotate(a + z, 52);
        c = rotate(a, 37);
        a += unalignedLOAD64(s.substring(s.length() - 24));
        c += rotate(a, 7);
        a += unalignedLOAD64(s.substring(s.length() - 16));
        long wf = a + z;
        long ws = b + rotate(a, 31) + c;
        long r = shiftMix((vf + ws) * k2 + (wf + vs) * k0);
        return shiftMix(r * k0 + vs) * k2;
    }

    static Pair<Long, Long> weakHashLen32WithSeeds(
            long w, long x, long y, long z, long a, long b) {
        a += w;
        b = rotate(b + a + z, 21);
        long c = a;
        a += x;
        a += y;
        b += rotate(a, 44);
        return new Pair<Long,Long>(a + z, b + c);
    }


    static Pair<Long, Long> weakHashLen32WithSeeds(
            String s, long a, long b) {
        return weakHashLen32WithSeeds(unalignedLOAD64(s),
                unalignedLOAD64(s.substring(8)),
                unalignedLOAD64(s.substring(16)),
                unalignedLOAD64(s.substring(24)),
                a,
                b);
    }

    Pair<Long,Long> doSwap(long a, long b){
        return new Pair<Long, Long>(b,a);
    }
    long cityHash64(String s) {
        if (s.length() <= 32) {
            if (s.length() <= 16) {
                return hashLen0to16(s);
            } else {
                return hashLen17to32(s);
            }
        } else if (s.length() <= 64) {
            return hashLen33to64(s);
        }

        // For strings over 64 bytes we catboost.hash the end first, and then as we
        // loop we keep 56 bytes of state: v, w, x, y, and z.
        long x = unalignedLOAD64(s);
        long y = unalignedLOAD64(s.substring(s.length() - 16)) ^ k1;
        long z = unalignedLOAD64(s.substring(s.length() - 56)) ^ k0;
        Pair<Long, Long> v = weakHashLen32WithSeeds(s.substring(s.length() - 64), s.length(), y);
        Pair<Long, Long> w = weakHashLen32WithSeeds(s.substring(s.length() - 32), s.length() * k1, k0);
        z += shiftMix(v.getValue()) * k1;
        x = rotate(z + x, 39) * k1;
        y = rotate(y, 33) * k1;

        // Decrease len to the nearest multiple of 64, and operate on 64-byte chunks.
        int len = (s.length() - 1) & ~(63);
        do {
            x = rotate(x + y + v.getKey() + unalignedLOAD64(s.substring(16)), 37) * k1;
            y = rotate(y + v.getValue() + unalignedLOAD64(s.substring(48)), 42) * k1;
            x ^= w.getValue();
            y ^= v.getKey();
            z = rotate(z ^ w.getKey(), 33);
            v = weakHashLen32WithSeeds(s, v.getValue() * k1, x + w.getKey());
            w = weakHashLen32WithSeeds(s.substring(32), z + w.getValue(), y);
            Pair<Long,Long> zx = doSwap(z, x);
            z = zx.getKey();
            x = zx.getValue();
            s  = s.substring(64);
            len -= 64;
        } while (len != 0);

        return hashLen16(hashLen16(v.getKey(), w.getKey()) + shiftMix(y) * k1 + z,
                hashLen16(v.getValue(), w.getValue()) + x);


  }



}
