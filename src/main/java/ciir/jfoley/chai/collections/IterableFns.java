package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.fn.SinkFn;
import ciir.jfoley.chai.fn.TransformFn;
import ciir.jfoley.chai.io.Closer;
import ciir.jfoley.chai.iters.ClosingIterator;
import ciir.jfoley.chai.iters.OneShotIterable;
import ciir.jfoley.chai.stream.ChaiStream;
import ciir.jfoley.chai.io.IO;

import java.util.*;

/**
 * Note that all these functions try to close their inputs if they consume the whole or part.
 * @see ciir.jfoley.chai.io.IO ::close()
 * @author jfoley.
 */
public class IterableFns {
	public static <A, B> Iterable<B> map(final Iterable<A> coll, final TransformFn<A,B> mapfn) {
		final Iterator<A> inner = coll.iterator();
		return ChaiStream.create(new ClosingIterator<B>() {
			@Override
			public void close() throws Exception {
				IO.close(inner);
			}

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
	public static <T, Coll extends Collection<T>> Coll collect(Iterable<T> input, Coll builder) {
		try (Closer<Iterable<T>> cc = Closer.of(input)) {
			for (T t : cc.get()) {
				builder.add(t);
			}
		}
		return builder;
	}

	public static <T> SinkFn<T> collect(Iterable<T> input, SinkFn<T> sink) {
		try (Closer<Iterable<T>> cc = Closer.of(input)) {
			for (T x : cc.get()) {
				sink.process(x);
			}
		}
		return sink;
	}

	public static <T extends Comparable> List<T> sorted(Collection<? extends T> input) {
		return sorted(input, Comparing.<T>defaultComparator(), new ArrayList<T>());
	}
	public static <T extends Comparable> List<T> sorted(Collection<? extends T> input, List<T> builder) {
		return sorted(input, Comparing.<T>defaultComparator(), builder);
	}

	public static <T> List<T> sorted(Collection<? extends T> input, Comparator<T> cmp) {
		return sorted(input, cmp, new ArrayList<T>());
	}
	public static <T> List<T> sorted(Collection<? extends T> input, Comparator<T> cmp, List<T> builder) {
		try (Closer<Collection<? extends T>> cc = Closer.<Collection<? extends T>>of(input)) {
			builder.addAll(input);
		}
		Collections.sort(builder, cmp);
		return builder;
	}

	public static <T> Iterator<T> lazyConcat(final Iterator<T> first, final Iterator<T> second) {
		return new ClosingIterator<T>() {
			public int state = 0;

			@Override
			public void close() throws Exception {
				IO.close(first);
				IO.close(second);
			}

			@Override
			public boolean hasNext() {
				return first.hasNext() || second.hasNext();
			}

			@Override
			public T next() {
				if(first.hasNext()) {
					return first.next();
				}
				if(second.hasNext()) {
					state = 1;
					return second.next();
				}
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				if(state == 1) {
					second.remove();
				} else {
					first.remove();
				}
			}
		};
	}

	public static <T> Iterable<T> lazyConcat(Iterable<T> first, Iterable<T> second) {
		return new OneShotIterable<>(lazyConcat(first.iterator(), second.iterator()));
	}

	public static <T> List<T> intoList(Iterable<T> of) {
		return collect(of, new ArrayList<T>());
	}
}
