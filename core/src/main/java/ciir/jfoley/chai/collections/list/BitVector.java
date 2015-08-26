package ciir.jfoley.chai.collections.list;

import java.io.PrintStream;
import java.util.Arrays;

/**
 * @author jfoley
 */
public class BitVector {
  long[] words;

  public BitVector(int numBits) {
    if(numBits <= 0) throw new IllegalArgumentException("Expected positive number of bits: "+numBits);
    int numWords = numBits / 64;
    if(numBits % 64 > 0) numWords++;
    words = new long[numWords];
  }

  public int count() {
    int count = 0;
    for (long word : words) {
      count += Long.bitCount(word);
    }
    return count;
  }

  public int size() {
    return words.length * 64;
  }
  public void set(int x, boolean val) {
    if(val) { set(x); } else { clear(x); }
  }

  public void clear() {
    Arrays.fill(this.words, 0);
  }

  public void clear(int x) {
    int wordIndex = x / 64;
    int bitIndex = x % 64;
    words[wordIndex] &= ~(1L << bitIndex);
  }
  public void set(int x) {
    int wordIndex = x / 64;
    int bitIndex = x % 64;
    words[wordIndex] |= (1L << bitIndex);
  }

  /**
   * Set a bunch of bits from start to end exclusive.
   * @param start inclusive start
   * @param end exclusive end
   */
  public void set(int start, int end) {
    int startWordIndex = start / 64;
    int endWordIndex = (end-1) / 64;

    if(startWordIndex != endWordIndex) {
      // fill all the bits in the overlapping words:
      Arrays.fill(this.words, startWordIndex + 1, endWordIndex, -1);
      set(startWordIndex, start % 64, 63);
      set(endWordIndex, 0, end % 64);
    }
    set(startWordIndex, start % 64, end % 64);
  }
  /**
   * Clear a bunch of bits from start to end exclusive.
   * @param start inclusive start
   * @param end exclusive end
   */
  public void clear(int start, int end) {
    int startWordIndex = start / 64;
    int endWordIndex = (end-1) / 64;

    if(startWordIndex != endWordIndex) {
      // fill all the bits in the overlapping words:
      Arrays.fill(this.words, startWordIndex + 1, endWordIndex, 0);
      clear(startWordIndex, start % 64, 63);
      clear(endWordIndex, 0, end % 64);
    }
    clear(startWordIndex, start % 64, end % 64);
  }

  private void set(int wordIndex, int startBit, int endBit) {
    int numBits = endBit - startBit;
    long mask = ((1L << numBits) - 1) << startBit;
    words[wordIndex] |= mask;
  }
  private void clear(int wordIndex, int startBit, int endBit) {
    int numBits = endBit - startBit;
    long mask = ((1L << numBits) - 1) << startBit;
    words[wordIndex] &= ~mask;
  }

  public IntList extract() {
    IntList output = new IntList(count());
    for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
      int shift = wordIndex*64;
      long w = words[wordIndex];
      int numSetBits = Long.bitCount(w);
      for (int bitNum = 0; bitNum < numSetBits; bitNum++) {
        int pos = Long.numberOfTrailingZeros(w);
        output.add(pos+shift);
        w &= ~(1L << pos);
      }
    }
    return output;
  }

  public void debug(PrintStream out) {
    for (long word : words) {
      out.printf("%016x ", word);
    }
    out.println();
  }

  public void shiftLeft(int amount) {
    if(amount == 0) return;
    assert(amount < 64 && amount > 0);
    words[0] >>>= amount;

    int off = (64 - amount);
    long mask = ((1L << amount) - 1) << off;
    for (int i = 1; i < words.length; i++) {
      long wn = Long.rotateRight(words[i], amount);
      words[i-1] |= (mask & wn);
      words[i] = wn & ~mask;
    }
  }

  public void or(BitVector other) {
    assert(this.words.length >= other.words.length);
    for (int i = 0; i < other.words.length; i++) {
      words[i] |= other.words[i];
    }
    // X or 0 = X
  }
  public void and(BitVector other) {
    assert(this.words.length >= other.words.length);
    for (int i = 0; i < other.words.length; i++) {
      words[i] &= other.words[i];
    }
    // X and 0 = 0
    Arrays.fill(words, other.words.length, words.length, 0);
  }
  public void xor(BitVector other) {
    assert(this.words.length >= other.words.length);
    for (int i = 0; i < other.words.length; i++) {
      words[i] ^= other.words[i];
    }
    // X xor 0 = X
  }
  public void complement() {
    for (int i = 0; i < words.length; i++) {
      words[i] = ~words[i];
    }
  }
}
