package ciir.jfoley.chai.random;

/*
Adapted from: http://xoroshiro.di.unimi.it/xoroshiro128plus.c

 Written in 2016 by David Blackman and Sebastiano Vigna (vigna@acm.org)

To the extent possible under law, the author has dedicated all copyright
and related and neighboring rights to this software to the public domain
worldwide. This software is distributed without any warranty.

See <http://creativecommons.org/publicdomain/zero/1.0/>. */


public class Xoroshiro128Plus implements RandGen {
    long s1;
    long s2;

    Xoroshiro128Plus() {
        this(System.nanoTime() ^ Thread.currentThread().getId());
    }
    Xoroshiro128Plus(long seed) {
        // turn seed into more entropy to cover state:
        final SplitMix64 simple = new SplitMix64(seed);
        s1 = simple.nextLong();
        s2 = simple.nextLong();
    }

    @Override
    public long nextLong() {
        long z0 = s1;
        long z1 = s2;
        long result = z0 + z1;

        z1 ^= z0;
        s1 = Long.rotateLeft(z0, 55) ^ z1 ^ (z1 << 14); // a, b
        s2 = Long.rotateLeft(z1, 36); // c

        return result;
    }
}
