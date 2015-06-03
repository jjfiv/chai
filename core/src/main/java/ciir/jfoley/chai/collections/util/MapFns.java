package ciir.jfoley.chai.collections.util;

import ciir.jfoley.chai.collections.Pair;
import ciir.jfoley.chai.fn.GenerateFn;
import ciir.jfoley.chai.fn.TransformFn;
import ciir.jfoley.chai.lang.Module;

import java.util.*;

/**
 * @author jfoley.
 */
public class MapFns extends Module {
	public static <K,V> V firstValue(Map<K,V> input) {
		if(input.isEmpty()) return null;
		return input.values().iterator().next();
	}

	public static <K,V> K firstKey(Map<K,V> input) {
		if(input.isEmpty()) return null;
		return input.keySet().iterator().next();
	}

	@SuppressWarnings("unchecked")
	public static <A,B> Map<A,B> cast(Map input) {
		return (Map<A,B>) input;
	}

	public static <K,T, Coll extends Collection<? super T>> void extendCollectionInMap(Map<? super K,Coll> inMap, K key, T value, Coll builder) {
		Coll existing = inMap.get(key);
		if(existing == null) {
			existing = builder;
			inMap.put(key, existing);
		}
		existing.add(value);
	}

	/**
	 * More efficient version of above.
	 * @param inMap the map.
	 * @param key the key.
	 * @param value the value to add to the collection.
	 * @param builder a generateFn that returns a collection of the appropriate type.
	 * @param <K> the key type.
	 * @param <T> the item type in the collection.
	 * @param <Coll> the type of the collection, extends Collection&lt;T&gt;
	 */
	public static <K,T, Coll extends Collection<? super T>> void extendCollectionInMap(Map<? super K,Coll> inMap, K key, T value, GenerateFn<Coll> builder) {
		Coll existing = inMap.get(key);
		if(existing == null) {
			existing = builder.get();
			inMap.put(key, existing);
		}
		existing.add(value);
	}

	public static <K,T> void extendListInMap(Map<K, List<T>> inMap, K key, T value) {
		extendCollectionInMap(inMap, key, value, new ArrayList<T>());
	}
	public static <K,T> void extendSetInMap(Map<K, Set<T>> inMap, K key, T value) {
		extendCollectionInMap(inMap, key, value, new HashSet<T>());
	}

	public static <K,V,VN> Map<K,VN> mapValues(Map<K, V> initial, TransformFn<V,VN> xfn) {
		return mapValues(initial, xfn, new HashMap<K, VN>(initial.size()));
	}

	public static <K,V,VN> Map<K,VN> mapValues(Map<K, V> initial, TransformFn<V,VN> xfn, Map<K,VN> builder) {
		for (Map.Entry<K, V> kv : initial.entrySet()) {
			builder.put(kv.getKey(), xfn.transform(kv.getValue()));
		}
		return builder;
	}

	public static <K, KN, V> Map<KN, V> mapKeys(Map<K, V> initial, TransformFn<K, KN> keyXFn) {
		return mapKeys(initial, keyXFn, new HashMap<KN, V>(initial.size()));
	}

	public static <K, KN, V> Map<KN, V> mapKeys(Map<K, V> initial, TransformFn<K, KN> keyXFn, Map<KN, V> builder) {
		for (Map.Entry<K, V> kv : initial.entrySet()) {
			builder.put(keyXFn.transform(kv.getKey()), kv.getValue());
		}
		return builder;
	}

	public static <K,V> Iterable<Pair<K,V>> pairs(Map<K,V> input) {
		return IterableFns.map(input.entrySet(), new TransformFn<Map.Entry<K,V>, Pair<K,V>>() {
			@Override
			public Pair<K, V> transform(Map.Entry<K, V> input) {
				return Pair.of(input);
			}
		});
	}

	public static <K,V> Map<V,K> invert(Map<K,V> input) {
		return invert(input, new HashMap<V, K>(input.size()));
	}
	public static <K,V> Map<V,K> invert(Map<K,V> input, Map<V,K> builder) {
		for (Map.Entry<K, V> kv : input.entrySet()) {
			builder.put(kv.getValue(), kv.getKey());
		}
		return builder;
	}

	public static <V> Map<Integer, V> ofListIndex(List<V> input) {
		return ofListIndex(input, new HashMap<Integer, V>(input.size()));
	}
	/** Todo, make this more efficient */
	public static <V> Map<Integer, V> ofListIndex(List<V> input, Map<Integer, V> builder) {
		for (int i = 0; i < input.size(); i++) {
			builder.put(i, input.get(i));
		}
		return builder;
	}

	public static <K> void addOrIncrement(Map<K,Integer> target, K key, int amt) {
		Integer prev = target.get(key);
		int next;
		if(prev == null) {
			next = amt;
		} else {
			next = prev + amt;
		}
		target.put(key, next);
	}

	public static <K,V> V getOrElse(Map<K, V> coll, K key, V defaultValue) {
		V maybeValue = coll.get(key);
		if(maybeValue == null) return defaultValue;
		return maybeValue;
	}

	public static <K,V> Map<K,V> fromPairs(Collection<Pair<K, V>> inBulk) {
		HashMap<K,V> map = new HashMap<>(inBulk.size());
		for (Pair<K, V> pair : inBulk) {
			map.put(pair.left, pair.right);
		}
		return map;
	}
}
