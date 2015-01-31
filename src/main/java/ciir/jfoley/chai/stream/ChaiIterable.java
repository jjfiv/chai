package ciir.jfoley.chai.stream;

import ciir.jfoley.chai.collections.Comparing;
import ciir.jfoley.chai.collections.IterableFns;
import ciir.jfoley.chai.fn.PredicateFn;
import ciir.jfoley.chai.fn.TransformFn;
import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.iters.OneShotIterable;

import java.util.*;

/**
 * @author jfoley.
 */
public class ChaiIterable<T> implements Iterable<T>, AutoCloseable {
	private final Iterable<T> iterable;

	protected ChaiIterable(Iterable<T> iter) {
		this.iterable = iter;
	}

	@Override
	public Iterator<T> iterator() {
		return iterable.iterator();
	}

	public <NT> ChaiIterable<NT> map(TransformFn<T, NT> transformFn) {
		return create(IterableFns.map(this, transformFn));
	}

	public ChaiIterable<T> filter(PredicateFn<T> predicate) {
		return create(IterableFns.filter(this, predicate));
	}

	public <K> Map<K, List<T>> groupBy(TransformFn<T,K> keyFn) {
		return IterableFns.groupBy(this, keyFn);
	}

	public ChaiIterable<T> sorted(Comparator<T> cmp) {
		return create(IterableFns.sorted(intoList(), cmp));
	}
	public ChaiIterable<T> sorted() {
		return create(IterableFns.sorted(intoList(), Comparing.<T>defaultComparator()));
	}

	public List<T> intoList() {
		return IterableFns.collect(this, new ArrayList<T>());
	}

	public <Coll extends Collection<T>> Coll collect(Coll builder) {
		return IterableFns.collect(this, builder);
	}

	public static <T> ChaiIterable<T> create(Iterator<T> iter) {
		return new ChaiIterable<>(new OneShotIterable<>(iter));
	}
	public static <T> ChaiIterable<T> create(Iterable<T> iter) {
		return new ChaiIterable<>(iter);
	}

	@SafeVarargs
	public static <T> ChaiIterable<T> create(T... inline) {
		List<T> asList = Arrays.asList(inline);
		return create(asList);
	}

	@Override
	public void close() throws Exception {
		IO.close(this.iterable);
	}
}
