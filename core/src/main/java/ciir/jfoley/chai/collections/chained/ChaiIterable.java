package ciir.jfoley.chai.collections.chained;

import ciir.jfoley.chai.Checked;
import ciir.jfoley.chai.collections.Pair;
import ciir.jfoley.chai.collections.iters.OneShotIterable;
import ciir.jfoley.chai.collections.util.Comparing;
import ciir.jfoley.chai.collections.util.IterableFns;
import ciir.jfoley.chai.fn.PredicateFn;
import ciir.jfoley.chai.fn.SinkFn;
import ciir.jfoley.chai.fn.TransformFn;
import ciir.jfoley.chai.io.IO;

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

	public <K> ChaiMap<K, List<T>> groupBy(TransformFn<T,K> keyFn) {
		return ChaiMap.create(IterableFns.groupBy(this, keyFn));
	}

	public ChaiIterable<T> sorted(Comparator<? super T> cmp) {
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

  public <X extends SinkFn<T>> X collect(X sink) {
    return IterableFns.collect(this, sink);
  }

  public ChaiIterable<T> maxK(int k) {
    return maxK(k, Comparing.<T>defaultComparator());
  }
  public ChaiIterable<T> maxK(int k, Comparator<? super T> cmp) {
    return create(IterableFns.maxK(this, k, cmp));
  }
  public ChaiIterable<T> minK(int k) {
    return minK(k, Comparing.<T>defaultComparator());
  }
  public ChaiIterable<T> minK(int k, Comparator<? super T> cmp) {
    return create(IterableFns.minK(this, k, cmp));
  }

  public ChaiIterable<List<T>> batches(int size) {
    return create(IterableFns.batches(this, size));
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

	public <K,V> ChaiMap<K,V> intoMap() {
		ChaiMap<K,V> map = ChaiMap.create();
		for (T t : this) {
			Pair<K,V> pair = Checked.cast(t);
			map.put(pair.left, pair.right);
		}
		return map;
	}

	public Set<T> intoSet() {
		return collect(new HashSet<T>());
	}
}
