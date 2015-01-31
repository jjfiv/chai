package ciir.jfoley.chai.collections;

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
}
