package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.lang.Module;

import javax.annotation.Nonnull;
import java.util.Comparator;

/**
 * @author jfoley.
 */
public class Comparing extends Module {
  @SuppressWarnings("unchecked")
  @Nonnull
	public static <T> Comparator<T> defaultComparator() {
		return (lhs, rhs) -> {
      //assert(lhs instanceof Comparable) : lhs.getClass().getName()+ " is not comparable!";
      return ((Comparable) lhs).compareTo(rhs);
    };
	}

  @SuppressWarnings("unchecked")
  @Nonnull
  public static <T> Comparator<T> reverseComparator() {
    return (lhs, rhs) -> ((Comparable) rhs).compareTo(lhs);
  }

  public static Comparator<byte[]> byteArrays() {
    return ArrayFns::compare;
  }

  public static <T> Comparator<T> reversed(final Comparator<T> cmp) {
    return (lhs, rhs) -> cmp.compare(rhs, lhs);
  }

  public static <T> Comparator<? super T> chained(final Comparator<? super T> first, final Comparator<? super T> then) {
    return (o1, o2) -> {
      int cmp = first.compare(o1, o2);
      if(cmp != 0) return cmp;
      return then.compare(o1, o2);
    };
  }
}
