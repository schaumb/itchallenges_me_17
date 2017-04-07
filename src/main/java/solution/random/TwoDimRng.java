package solution.random;

import java.util.function.UnaryOperator;

/**
 * Created by qqcs on 06/04/17.
 */
public class TwoDimRng {
    private final long hashed_seed;
    private final UnaryOperator<Long> hash;
    public static final UnaryOperator<Long> DEFAULT_HASH = n -> {
        n = (n ^ (n >>> 30)) * 0xbf58476d1ce4e5b9L;
        n = (n ^ (n >>> 27)) * 0x94d049bb133111ebL;
        n = n ^ (n >>> 31);
        return n;
    };


    public TwoDimRng(long seed) {
        this(seed, DEFAULT_HASH);
    }

    public TwoDimRng(long seed, UnaryOperator<Long> hash) {
        this.hash = hash;
        this.hashed_seed = hash.apply(seed);
    }

    public long get(long x, long y) {
        return hashed_seed ^ hash.apply(x ^ hash.apply(y));
    }
}
