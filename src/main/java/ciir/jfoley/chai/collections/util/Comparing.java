package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.lang.Module;

import java.util.Comparator;

/**
 * @author jfoley.
 */
public class Comparing extends Module {
	public static <T> Comparator<T> defaultComparator() {
		return new Comparator<T>() {
			@Override
			@SuppressWarnings("unchecked")
			public int compare(T lhs, T rhs) {
				assert(lhs instanceof Comparable);
				return ((Comparable) lhs).compareTo(rhs);
			}
		};
	}

  public static <T> Comparator<T> reverseComparator() {
    return new Comparator<T>() {
      @Override
      @SuppressWarnings("unchecked")
      public int compare(T lhs, T rhs) {
        assert(lhs instanceof Comparable);
        return ((Comparable) rhs).compareTo(lhs);
      }
    };
  }

  public static <T> Comparator<T> reversed(final Comparator<T> cmp) {
    return new Comparator<T>() {
      @Override
      public int compare(T lhs, T rhs) {
        return cmp.compare(rhs, lhs);
      }
    };
  }
}
