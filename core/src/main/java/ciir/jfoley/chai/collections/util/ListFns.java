package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.collections.Pair;
import ciir.jfoley.chai.lang.Module;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;

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
      output.add(input.subList(i*numberPerSplit, Math.min(input.size(), (i+1)*numberPerSplit)));
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
}
