package ciir.jfoley.chai.collections.util;

import java.util.AbstractList;
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
}
