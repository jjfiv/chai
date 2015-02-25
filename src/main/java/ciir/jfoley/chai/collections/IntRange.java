package ciir.jfoley.chai.collections;

import java.util.AbstractList;
import java.util.NoSuchElementException;

/**
 * Constant memory consecutive integer ranges.
 * @author jfoley
 */
public class IntRange extends AbstractList<Integer> {
  private final int start;
  private final int size;

  public IntRange(int start, int size) {
    this.start = start;
    this.size = size;
  }

  public int inclusiveEnd() {
    return start + size - 1;
  }
  public int exclusiveEnd() {
    return start + size;
  }

  @Override
  public boolean contains(Object x) {
    return x instanceof Integer && containsInt((Integer) x);
  }

  public boolean containsInt(int val) {
    return val >= start && val < exclusiveEnd();
  }

  @Override
  public Integer get(int index) {
    if(index >= size) throw new NoSuchElementException();
    return start + index;
  }

  @Override
  public int size() {
    return size;
  }

  public static IntRange inclusive(int begin, int end) {
    return new IntRange(begin, end-begin+1);
  }
  public static IntRange exclusive(int begin, int end) {
    return new IntRange(begin, end-begin);
  }
}
