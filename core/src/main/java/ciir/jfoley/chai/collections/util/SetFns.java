package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.lang.Module;
import gnu.trove.set.hash.TIntHashSet;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jfoley.
 */
public class SetFns extends Module {

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

	public static double jaccardIndex(TIntHashSet a, TIntHashSet b) {
		TIntHashSet union = new TIntHashSet(a);
		union.addAll(b);

		// count up intersection:
		AtomicInteger count = new AtomicInteger();
		a.forEach(x -> {
			if(b.contains(x)) {
				count.incrementAndGet();
			}
			return true;
		});

		double unionSize = union.size();
		double isectSize = count.get();
		return isectSize / unionSize;
	}

	public static <T> double jaccardIndex(Collection<T> a, Collection<T> b) {
		double unionSize = union(a,b).size();
		if(unionSize == 0) return 0;
		double isectSize = intersection(a, b).size();
		return isectSize / unionSize;
	}

	public static <T> double jaccardDistance(Collection<T> a, Collection<T> b) {
		return 1.0 - jaccardIndex(a, b);
	}

	public static <T> double diceCoefficient(Collection<T> a, Collection<T> b) {
		double isectSize = intersection(a, b).size();
		return 2 * isectSize / (a.size() + b.size());
	}

	/** Return the items in A that are not in B. */
	public static <T> Set<T> difference(Collection<T> A, Collection<T> B) {
		HashSet<T> results = new HashSet<>(A);
		results.removeAll(B);
		return results;
	}
}
