package ciir.jfoley.chai.collections.util;

import java.util.*;

/**
 * @author jfoley.
 */
public class SetFns {

	public static <T> Set<T> intersection(List<? extends Collection<? extends T>> sets) {
		if(sets.isEmpty()) return Collections.emptySet();

		Set<T> accum = new HashSet<>();
		accum.addAll(sets.get(0));
		for(int i=1; i<sets.size(); i++) {
			accum = intersection(accum, sets.get(i));
		}
		return accum;
	}

	public static <T> Set<T> intersection(Collection<? extends T> lhs, Collection<? extends T> rhs) {
		Collection<? extends T> minSet = lhs.size() < rhs.size() ? lhs : rhs;
		Collection<? extends T> maxSet = lhs.size() < rhs.size() ? rhs : lhs;

		HashSet<T> isect = new HashSet<>();
		for(T x : minSet) {
			if(maxSet.contains(x)) {
				isect.add(x);
			}
		}
		return isect;
	}

	public static <T> Set<T> union(Collection<T> lhs, Collection<T> rhs) {
		HashSet<T> results = new HashSet<>(lhs.size() + rhs.size());
		results.addAll(lhs);
		results.addAll(rhs);
		return results;
	}
}
