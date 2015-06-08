package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.collections.ListBasedOrderedSet;
import ciir.jfoley.chai.collections.Pair;
import ciir.jfoley.chai.fn.PredicateFn;
import ciir.jfoley.chai.fn.TransformFn;
import ciir.jfoley.chai.lang.Module;

import java.util.*;

/**
 * This module contains a number of functions meant to operate on lists (sometimes it's much easier than the related iterables)
 * {@link IterableFns}
 * @author jfoley.
 */
public class ListFns extends Module {
	/** Specialized lazy List concat */
	public static <T> List<T> lazyConcat(final List<T> first, final List<T> second) {
		return new AbstractList<T>() {
			@Override
			public T get(int i) {
				if(i < first.size()) {
					return first.get(i);
				}
				return second.get(i - first.size());
			}

			@Override
			public int size() {
				return first.size() + second.size();
			}
		};
	}

  /** Take a view of up to amt items from the front of a list */
  public static <T> List<T> take(List<T> input, int amt) {
    return input.subList(0, Math.min(input.size(), amt));
  }

  /**
   * Returns a sliding window of subList views over the input list of size window.
   * @param input The input list to iterate over.
   * @param window The size of the window.
   * @param <T> The type parameter of the input list.
   * @return A list of windows to process further.
   */
  public static <T> List<List<T>> sliding(List<T> input, int window) {
    List<List<T>> windows = new ArrayList<>(input.size());
    for (int start = 0; (start+window-1) < input.size(); start++) {
      int end = start + window; // inclusive
      windows.add(input.subList(start, end));
    }
    return windows;
  }

  /**
   * Returns a list of pairs, all pairs, assuming order doesn't matter.
   * @param input The input list to split up.
   * @param <T> the parameter of the input list.
   * @return All unique order-independent pairs in the list.
   */
  public static <T> List<Pair<T,T>> pairs(List<T> input) {
    input = ensureRandomAccess(input);
    List<Pair<T,T>> output = new ArrayList<>(input.size()*(input.size()-1));
    for (int i = 0; i < input.size()-1; i++) {
      for (int j = i+1; j < input.size(); j++) {
        output.add(Pair.of(input.get(i), input.get(j)));
      }
    }
    return output;
  }

  /**
   * Break a list into pieces in order. Actually creates subList views of the original list.
   * {@link java.util.List#subList}
   *
   * @param input The input list to break into pieces.
   * @param splits the number of partitions to make of the original.
   * @return A list of subLists.
   */
  public static <T> List<List<T>> partition(List<T> input, int splits) {
    input = ensureRandomAccess(input);
    int numberPerSplit = (int) Math.round(input.size() / (double) splits);
    List<List<T>> output = new ArrayList<>();
    for (int i = 0; i < splits; i++) {
      output.add(slice(input, i * numberPerSplit, (i + 1) * numberPerSplit));
    }
    // fix up a small remainder
    if(input.size() > splits * numberPerSplit) {
      output.set(splits-1, input.subList((splits-1) * numberPerSplit, input.size()));
    }
    return output;
  }

  /**
   * Break a list into pieces in order. Actually creates subList views of the original list.
   * {@link java.util.List#subList}
   *
   * @param input The input list to break into pieces.
   * @param splits the number of partitions to make of the original.
   * @return A list of subLists.
   */
  public static <T> List<List<T>> partitionRoundRobin(List<T> input, int splits) {
    input = ensureRandomAccess(input);
    List<List<T>> output = new ArrayList<>();
    for (int i = 0; i < splits; i++) {
      output.add(new ArrayList<T>(input.size() / splits));
    }
    for (int i = 0; i < input.size(); i++) {
      output.get(i%splits).add(input.get(i));
    }
    return output;
  }

  /** Copy this list into an ArrayList is if cannot be quickly accessed by index. */
  public static <T> List<T> ensureRandomAccess(List<? extends T> input) {
    if(input instanceof RandomAccess) return castView(input);
    return new ArrayList<>(input);
  }

  public static <T> List<T> castView(final List<? extends T> input) {
    return new AbstractList<T>() {
      @Override
      public T get(int index) {
        return input.get(index);
      }

      @Override
      public int size() {
        return input.size();
      }
    };
  }

  /**
   * Collect an Enumeration into a List, so you can do a for loop like a normal person.
   * @param entries The abomination.
   * @param <T> The type of contained values.
   * @return A list of the contained values. Makes a copy.
   */
  public static <T> List<T> collect(Enumeration<? extends T> entries) {
    List<T> results = new ArrayList<>();
    while (entries.hasMoreElements()) {
      results.add(entries.nextElement());
    }
    return results;
  }

  /**
   * Append to a shallow copy of this list; immutable .add()
   * @param original the input list.
   * @param newItem the new item.
   * @param <T> the type of items.
   * @return a new list containing the original items + the new item.
   */
  public static <T> List<T> pushToCopy(List<T> original, T newItem) {
    List<T> newList = new ArrayList<>(original);
    newList.add(newItem);
    return newList;
  }

  public static <T> boolean matches(List<T> input, PredicateFn<T> condition) {
    return findFirst(input, condition) != null;
  }

  public static <T> T findFirst(List<T> input, PredicateFn<T> condition) {
    for (T t : input) {
      if(condition.test(t)) {
        return t;
      }
    }
    return null;
  }

  /**
   * Find the max of a collection by a given transform function.
   * @param objs the collection.
   * @param fn the function that takes an object and returns a comparable property.
   * @param <T> the type of objects in the collection.
   * @param <V> the type of comparable objects.
   * @return the maximum T by V.
   */
  public static <T, V extends Comparable<V>> T maxBy(List<? extends T> objs, TransformFn<T,V> fn) {
    if(objs.isEmpty()) return null;
    T max = objs.get(0);
    V maxValue = fn.transform(max);
    for (int i = 1; i < objs.size(); i++) {
      T t = objs.get(i);
      V tv = fn.transform(t);
      if(maxValue.compareTo(tv) < 0) {
        max = t;
        maxValue = tv;
      }
    }
    return max;
  }

  /**
   * Remove duplicates from an input list.
   * @param input the list.
   * @param <T> the type of elements in the list.
   * @return an ordered de-duplication. Might not be as fast as unordered.
   */
  public static <T> List<T> unique(List<? extends T> input) {
    return new ListBasedOrderedSet<>(input).toList();
  }

  /**
   * Joins two collections up to their minimum size, copying into a new list.
   * @param lhs the left hand collection.
   * @param rhs the right hand collection.
   * @param <A> type of left objects.
   * @param <B> type of right objects.
   * @return a list of pairs of equivalent-indexed elements.
   */
  public static <A,B> List<Pair<A,B>> zip(List<? extends A> lhs, List<? extends B> rhs) {
    List<Pair<A,B>> output = new ArrayList<>();

    int shared = Math.min(lhs.size(), rhs.size());
    for (int i = 0; i < shared; i++) {
      output.add(Pair.of(lhs.get(i), rhs.get(i)));
    }

    return output;
  }

  /**
   * Takes a sublist, whether there are items inside or not, avoiding out-of-bounds errors.
   * Note that ends are exclusive in Java's subList and here too.
   *
   * @param input the list to splice.
   * @param start the start (may be negative)
   * @param end the end (may be above input.size());
   * @param <T> the type of the list.
   * @return a sublist approximating the request as best as possible.
   */
  public static <T> List<T> slice(List<T> input, int start, int end) {
    int realStart = Math.max(0, start);
    int realEnd = Math.min(end, input.size());
    return input.subList(realStart, realEnd);
  }


  /**
   * When you have some set of items and you want to repeat them until you have at least X of them.
   * @param input possibly small number of items.
   * @param wanted the number of items to cycle these to at least.
   * @param <T> the item type.
   * @return a list of input repeated 0+ times so that the total length >= wanted.
   */
  public static <T> List<T> repeatUntilAtLeast(List<T> input, int wanted) {
    if(input.size() == 0) throw new IllegalArgumentException("Can't repeat zero items!");
    if(input.size() >= wanted) return input;
    ArrayList<T> output = new ArrayList<>(wanted);
    while(output.size() < wanted) {
      output.addAll(input);
    }
    return output;
  }
}
