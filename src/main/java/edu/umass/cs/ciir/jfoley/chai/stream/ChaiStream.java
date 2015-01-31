package edu.umass.cs.ciir.jfoley.chai.stream;

import edu.umass.cs.ciir.jfoley.chai.collections.Fns;
import edu.umass.cs.ciir.jfoley.chai.fn.TransformFn;
import edu.umass.cs.ciir.jfoley.chai.iters.OneShotIterable;

import java.util.*;

/**
 * @author jfoley.
 */
public class ChaiStream<T> implements IChaiStream<T> {
	private final Iterable<T> iterable;
	private final long size;

	protected ChaiStream(Iterable<T> iter, long size) {
		this.size = size;
		this.iterable = iter;
	}

	protected ChaiStream(Iterable<T> iter) {
		this.size = IChaiStream.UNKNOWN_SIZE;
		this.iterable = iter;
	}

	@Override
	public long expectedSize() {
		return size;
	}

	@Override
	public Iterator<T> iterator() {
		return iterable.iterator();
	}

	@Override
	public <NT> IChaiStream<NT> map(TransformFn<T, NT> transformFn) {
		return create(Fns.map(this, transformFn));
	}

	@Override
	public IChaiStream<T> sorted(Comparator<T> cmp) {
		return create(Fns.sorted(intoList(), cmp));
	}
	@Override
	public IChaiStream<T> sorted() {
		return create(Fns.sorted(intoList(), Fns.<T>defaultComparator()));
	}

	public List<T> intoList() {
		return Fns.collect(this, new ArrayList<T>());
	}
	@Override
	public <Coll extends Collection<T>> Coll collect(Coll builder) {
		return Fns.collect(this, builder);
	}


	public static <T> IChaiStream<T> create(Iterator<T> iter) {
		return new ChaiStream<T>(new OneShotIterable<>(iter));
	}
	public static <T> IChaiStream<T> create(Iterable<T> iter) {
		return new ChaiStream<>(iter);
	}
	public static <T> IChaiStream<T> create(Collection<T> coll) {
		return new ChaiStream<>(coll, coll.size());
	}

	@SafeVarargs
	public static <T> IChaiStream<T> create(T... inline) {
		List<T> asList = Arrays.asList(inline);
		return create(asList);
	}

}
