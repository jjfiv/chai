package edu.umass.cs.ciir.jfoley.chai.collections;

import edu.umass.cs.ciir.jfoley.chai.fn.TransformFn;
import edu.umass.cs.ciir.jfoley.chai.stream.ChaiStream;

import java.util.*;

/**
 * @author jfoley.
 */
public class Fns {
	public static <A, B> Iterable<B> map(final Iterable<A> coll, final TransformFn<A,B> mapfn) {
		final Iterator<A> inner = coll.iterator();
		return ChaiStream.create(new Iterator<B>() {
			@Override
			public boolean hasNext() {
				return inner.hasNext();
			}

			@Override
			public B next() {
				return mapfn.transform(inner.next());
			}

			@Override
			public void remove() {
				inner.remove();
			}
		});
	}

	/** Collect results into the given collection */
	public static <T, Coll extends Collection<T>> Coll collect(Iterable<T> cs, Coll builder) {
		for (T t : cs) {
			builder.add(t);
		}
		return builder;
	}

	public static <T extends Comparable> List<T> sorted(Collection<? extends T> input) {
		return sorted(input, new ArrayList<T>());
	}
	public static <T extends Comparable> List<T> sorted(Collection<? extends T> input, List<T> builder) {
		builder.addAll(input);
		Collections.sort(builder);
		return builder;
	}

	public static <T> List<T> sorted(Collection<? extends T> input, Comparator<T> cmp) {
		return sorted(input, cmp, new ArrayList<T>());
	}
	public static <T> List<T> sorted(Collection<? extends T> input, Comparator<T> cmp, List<T> builder) {
		builder.addAll(input);
		Collections.sort(builder, cmp);
		return builder;
	}

}
