package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.Checked;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * This is a Map implementation that is optimized for a small number of K,V pairs.
 * It is stored in an arraylist, where
 * arraylist[0] = key[0]
 * arraylist[1] = value[0]
 * arraylist[2] = key[1]
 * arraylist[3] = value[1]
 * ...
 * arraylist[i] = key[i/2];
 * arraylist[i+1] = value[i/2];
 *
 * @author jfoley.
 */
public class ArrayListMap<K,V> implements Map<K,V> {
	private final ArrayList<Object> data;

	public ArrayListMap() {
		this(16);
	}
	public ArrayListMap(int size) {
		this.data = new ArrayList<>(size);
	}

	@Override
	public int size() {
		return data.size() / 2;
	}

	@Override
	public boolean isEmpty() {
		return data.size() == 0;
	}

	@Override
	public boolean containsKey(Object o) {
		return find(o) >= 0;
	}

	/** find index of key */
	int find(Object key) {
		for (int i = 0; i < data.size(); i+=2) {
			if(data.get(i).equals(key)) {
				return i;
			}
		}
		return -1;
	}

	V getByIndex(int keyIndex) {
		if(keyIndex >= 0) {
			return Checked.cast(data.get(keyIndex + 1));
		}
		return null;
	}


	@Override
	public boolean containsValue(Object o) {
		for (int i = 1; i < data.size(); i+=2) {
			if(data.get(i).equals(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public V get(Object o) {
		return getByIndex(find(o));
	}

	@Override
	public V put(K k, V v) {
		int index = find(k);
		if (index < 0) {
			data.add(k);
			data.add(v);
			return null;
		}

		V original = getByIndex(index);
		data.set(index+1, v);
		return original;
	}

	@Override
	public V remove(Object o) {
		int pos = find(o);
		if(pos == -1) {
			return null;
		}
		// remove key and value, in smart order.
		V original = getByIndex(pos);
		data.remove(pos + 1);
		data.remove(pos);
		return original;
	}

	@Override
	public void putAll(@Nonnull Map<? extends K, ? extends V> map) {
		for (Entry<? extends K, ? extends V> kv : map.entrySet()) {
			put(kv.getKey(), kv.getValue());
		}
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	@Nonnull
	public Set<K> keySet() {
		HashSet<K> keys = new HashSet<>();
		for (int i = 0; i < data.size(); i+=2) {
			keys.add(Checked.<K>cast(data.get(i)));
		}
		return keys;
	}

	@Override
	@Nonnull
	public Collection<V> values() {
		HashSet<V> vals = new HashSet<>();
		for (int i = 1; i < data.size(); i+=2) {
			vals.add(Checked.<V>cast(data.get(i)));
		}
		return vals;
	}

	@Override
	@Nonnull
	public Set<Entry<K, V>> entrySet() {
		HashSet<Entry<K,V>> vals = new HashSet<>();
		for (int i = 0; i < data.size(); i+=2) {
			vals.add(new AbstractMap.SimpleImmutableEntry<>(
				Checked.<K>cast(data.get(i)),
				Checked.<V>cast(data.get(i + 1))));
		}
		return vals;
	}

	@Override
	public String toString() {
		return new HashMap<>(this).toString();
	}

	@Override
	public int hashCode() {
		int hash = data.get(0).hashCode();
		for (int i = 1; i < data.size(); i++) {
			hash ^= data.get(i).hashCode();
		}
		return hash;
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object o) {
		if(o instanceof Map) {
			Map om = (Map) o;
			return size() == om.size() && entrySet().equals(om.entrySet());
		}
		return false;
	}

}
