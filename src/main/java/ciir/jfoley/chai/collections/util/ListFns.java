package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.collections.Pair;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jfoley.
 */
public class ListFns {
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

  public static <T> List<List<T>> sliding(List<T> input, int window) {
    List<List<T>> windows = new ArrayList<>(input.size());
    for (int start = 0; (start+window-1) < input.size(); start++) {
      int end = start + window; // inclusive
      windows.add(input.subList(start, end));
    }
    return windows;
  }
  public static <T> List<Pair<T,T>> pairs(List<T> input) {
    List<Pair<T,T>> output = new ArrayList<>(input.size()*(input.size()-1));
    for (int i = 0; i < input.size()-1; i++) {
      for (int j = i+1; j < input.size(); j++) {
        output.add(Pair.of(input.get(i), input.get(j)));
      }
    }
    return output;
  }

}
