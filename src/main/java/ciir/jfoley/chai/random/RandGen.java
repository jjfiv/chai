package ciir.jfoley.chai.random;

/**
 * Implementations in this file are inspired by OpenJDK's {@see java.util.Random}.
 * @author jfoley
 */
public interface RandGen {
    long nextLong();
    default int nextInt() {
        return (int) nextLong();
    }

    /**
     * {@link java.util.Random#nextInt(int)}
     * @param bound the upper limit on the numbers you want.
     * @return a number in the range [0,bound)
     */
    default int nextInt(int bound) {
        if(bound <= 0)
            throw new IllegalArgumentException("Can't get an int lower than or equal to zero, requested: "+bound);
        int r = nextBits(31);
        int m = bound - 1;
        if ((bound & m) == 0)  // i.e., bound is a power of 2
            r = (int)((bound * (long)r) >> 31);
        else {
            for (int u = r;
                 u - (r = u % bound) + m < 0;
                 u = nextBits(31)) {
                ; // pass
            }
        }
        return r;
    }
    default int nextBits(int bits) {
        long data = nextLong();
        return (int)(data >>> (48-bits));
    }
    default boolean nextBoolean() {
        return (nextLong() & 0x1L) > 0;
    }
}
