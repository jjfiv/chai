package ciir.jfoley.chai.collections;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* @author jfoley
*/
public class Pair<A,B> implements Map.Entry<A,B> {
  public final A left;
  public final B right;

  public Pair(A left, B right) {
    this.left = left;
    this.right = right;
  }
  public Pair(Map.Entry<A,B> input) {
    this(input.getKey(), input.getValue());
  }

  @Override
  public int hashCode() {
    return left.hashCode() ^ right.hashCode();
  }

  @Override
  public A getKey() {
    return left;
  }

  @Override
  public B getValue() {
    return right;
  }

  @Override
  public B setValue(B b) {
    throw new UnsupportedOperationException("Pair is immutable.");
  }

  @Override
  public String toString() {
    return "["+left+" "+right+"]";
  }

  @Override
  public boolean equals(Object other) {
    if(other == null) return false;
    if(!(other instanceof Pair)) return false;
    Pair cmp = (Pair) other;
    return left.equals(cmp.left) && right.equals(cmp.right);
  }

  public static <X,Y> Pair<X,Y> of(X lhs, Y rhs) {
    return new Pair<>(lhs, rhs);
  }

  public static <T> List<T> asList(Pair<T,T> input) {
    return Arrays.asList(input.left, input.right);
  }

  public static <A,B> Map<A,B> pairsToMap(Map<A,B> output, Iterable<Pair<A,B>> input) {
    assert(output != null);
    for (Pair<A, B> abPair : input) {
      output.put(abPair.left, abPair.right);
    }
    return output;
  }

}
