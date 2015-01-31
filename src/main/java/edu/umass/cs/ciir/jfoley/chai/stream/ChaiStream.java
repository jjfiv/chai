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
		return new ChaiStream<T>(iter);
	}
	public static <T> IChaiStream<T> create(Collection<T> coll) {
		return new ChaiStream<T>(coll, coll.size());
	}


}
