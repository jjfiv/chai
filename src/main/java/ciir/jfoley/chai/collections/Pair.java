package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.util.Comparing;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
* @author jfoley
*/
public class Pair<A,B> implements Map.Entry<A,B> {
  public final A left;
  public final B right;

  public Pair(final A left, final B right) {
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

  /**
   * Compare left first then right, only if the elements satisfy comparable.
   * Note that naively chaining left and right without checking for Comparable is a bad idea.
   * @return Comparator by left then right.
   */
  public Comparator<? super Pair<A, B>> getBestComparator() {
    Comparator<? super Pair<A,B>> leftCmp = (left instanceof Comparable) ? Pair.<A,B>cmpLeft() : null;
    Comparator<? super Pair<A,B>> rightCmp = (right instanceof Comparable) ? Pair.<A,B>cmpRight() : null;
    if (leftCmp != null && rightCmp != null) {
      return Comparing.chained(leftCmp, rightCmp);
    } else if(leftCmp != null) {
      return Pair.cmpLeft();
    } else if(rightCmp != null) {
      return Pair.cmpRight();
    }
    throw new IllegalArgumentException("Can't compare either element of Pair!");
  }

  public static <K, V> Pair<K,V> of(Map.Entry<K, V> input) {
    return new Pair<>(input);
  }

  public static <K, V> Comparator<? super Pair<K, V>> cmpRight(final Comparator<V> cmp) {
    return new Comparator<Map.Entry<K, V>>() {
      @Override
      public int compare(Map.Entry<K, V> lhs, Map.Entry<K, V> rhs) {
        return cmp.compare(lhs.getValue(), rhs.getValue());
      }
    };
  }

  public static <K, V> Comparator<? super Pair<K, V>> cmpLeft(final Comparator<K> cmp) {
    return new Comparator<Map.Entry<K, V>>() {
      @Override
      public int compare(Map.Entry<K, V> lhs, Map.Entry<K, V> rhs) {
        return cmp.compare(lhs.getKey(), rhs.getKey());
      }
    };
  }

  public static <K,V> Comparator<? super Pair<K, V>> cmpRight() {
    return cmpRight(Comparing.<V>defaultComparator());
  }
  public static <K,V> Comparator<? super Pair<K, V>> cmpLeft() {
    return cmpLeft(Comparing.<K>defaultComparator());
  }
}
