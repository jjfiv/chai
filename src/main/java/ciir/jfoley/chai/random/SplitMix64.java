package ciir.jfoley.chai.random;

/*
 Based on: http://xoroshiro.di.unimi.it/splitmix64.c

 Written in 2015 by Sebastiano Vigna (vigna@acm.org)

 To the extent possible under law, the author has dedicated all copyright
and related and neighboring rights to this software to the public domain
worldwide. This software is distributed without any warranty.

 See <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

/**
 * Really silly 64-bit random number generator.
 */
public class SplitMix64 implements RandGen {
    long state;

    public SplitMix64(long seed) {
        this.state = seed;
    }
    public SplitMix64() {
        this(System.nanoTime());
    }
    @Override
    public long nextLong() {
        long z = state += 0x9E3779B97F4A7C15L;
        z = (z ^ (z >> 30)) * 0xBF58476D1CE4E5B9L;
        z = (z ^ (z >> 27)) * 0x94D049BB133111EBL;
        return z ^ (z >> 31);
    }
}
