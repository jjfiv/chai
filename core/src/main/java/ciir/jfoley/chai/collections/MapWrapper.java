package ciir.jfoley.chai.collections;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author jfoley.
 */
public class MapWrapper<K,V> implements Map<K,V> {
	protected Map<K,V> inner;
	public MapWrapper(Map<K,V> inner) {
		this.inner = inner;
	}

	@Override
	public int size() {
		return inner.size();
	}

	@Override
	public boolean isEmpty() {
		return inner.isEmpty();
	}

	@Override
	public boolean containsKey(Object o) {
		return inner.containsKey(o);
	}

	@Override
	public boolean containsValue(Object o) {
		return inner.containsValue(o);
	}

	@Override
	public V get(Object o) {
		return inner.get(o);
	}

	@Override
	public V put(K k, V v) {
		return inner.put(k, v);
	}

	@Override
	public V remove(Object o) {
		return inner.remove(o);
	}

	@Override
	public void putAll(@Nonnull Map<? extends K, ? extends V> map) {
		inner.putAll(map);
	}

	@Override
	public void clear() {
		inner.clear();
	}

	@Override
	@Nonnull
	public Set<K> keySet() {
		return inner.keySet();
	}

	@Override
	@Nonnull
	public Collection<V> values() {
		return inner.values();
	}

	@Override
	@Nonnull
	public Set<Entry<K, V>> entrySet() {
		return inner.entrySet();
	}

	@Override
	public String toString() {
		return inner.toString();
	}

	@Override
	public int hashCode() {
		return inner.hashCode();
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object o) {
		return inner.equals(o);
	}
}
