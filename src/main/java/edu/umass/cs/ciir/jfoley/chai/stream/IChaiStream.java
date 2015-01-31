package edu.umass.cs.ciir.jfoley.chai.stream;

import edu.umass.cs.ciir.jfoley.chai.fn.TransformFn;

import java.util.Collection;
import java.util.List;

/**
 * @author jfoley.
 */
public interface IChaiStream<T> extends Iterable<T> {
	public long UNKNOWN_SIZE = -1;
	/** If known, UNKNOWN_SIZE if not. */
	public long expectedSize();

	public <NT> IChaiStream<NT> map(TransformFn<T, NT> transformFn);

	public List<T> intoList();
	public <Coll extends Collection<T>> Coll collect(Coll builder);
}
