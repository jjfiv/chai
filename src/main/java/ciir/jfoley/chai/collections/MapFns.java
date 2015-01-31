package ciir.jfoley.chai.collections;

import java.util.*;

/**
 * @author jfoley.
 */
public class MapFns {
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
	public static <K,T> void extendListInMap(Map<K, List<T>> inMap, K key, T value) {
		extendCollectionInMap(inMap, key, value, new ArrayList<T>());
	}
	public static <K,T> void extendSetInMap(Map<K, Set<T>> inMap, K key, T value) {
		extendCollectionInMap(inMap, key, value, new HashSet<T>());
	}

}
