package ciir.jfoley.chai.collections.chained;

import ciir.jfoley.chai.collections.MapWrapper;
import ciir.jfoley.chai.collections.Pair;
import ciir.jfoley.chai.collections.util.MapFns;
import ciir.jfoley.chai.io.IO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jfoley.
 */
public class ChaiMap<K,V> extends MapWrapper<K,V> implements AutoCloseable {
	Map<K,V> inner;

	ChaiMap(Map<K,V> inner) {
		super(inner);
	}

	@Override
	public void close() throws Exception {
		IO.close(inner);
	}

	public ChaiIterable<Pair<K,V>> pairs() {
		return ChaiIterable.create(MapFns.pairs(inner));
	}

	public V getOrElse(K key, V otherwise) {
		V tmp = inner.get(key);
		if(tmp == null) return otherwise;
		return tmp;
	}

	public ChaiMap<V,K> invert() {
		return create(MapFns.invert(inner));
	}
	public ChaiMap<V,K> invert(Map<V,K> builder) {
		return create(MapFns.invert(inner, builder));
	}

	public static <A,B> ChaiMap<A,B> create(Map<A,B> input) {
		return new ChaiMap<>(input);
	}

	public static <A,B> ChaiMap<A,B> create(Iterable<? extends Pair<A,B>> input, Map<A,B> builder) {
		for (Pair<A, B> kv : input) {
			builder.put(kv.left, kv.right);
		}
		return new ChaiMap<>(builder);
	}

	public static <A,B> ChaiMap<A,B> create(Iterable<? extends Pair<A,B>> input) {
		return create(input, new HashMap<A, B>());
	}

	@SafeVarargs
	public static <A,B> ChaiMap<A,B> create(Pair<A,B>... input) {
		return create(Arrays.asList(input));
	}

}
