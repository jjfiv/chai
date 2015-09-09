package ciir.jfoley.chai.collections.maps;

import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TLongIntHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.TLongSet;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author jfoley
 */
public class MapFactory {
  private static final Class<?> intClass = Integer.class;
  private static final Class<?> longClass = Long.class;

  @SuppressWarnings("unchecked")
  public static <K,V> Map<K,V> create(Class<?> keyClass, Class<?> valClass) {
    if(keyClass == intClass) {
      if(valClass == intClass) {
        return (Map<K,V>) new TroveIntIntMap(new TIntIntHashMap());
      }
    } else if(keyClass == longClass) {
      if(valClass == intClass) {
        return (Map<K,V>) new TroveLongIntMap(new TLongIntHashMap());
      }
      return (Map<K,V>) new TroveLongObjectMap<>(new TLongObjectHashMap<>());
    }
    return new HashMap<>();
  }

  public static class TroveIntIntMap implements Map<Integer, Integer> {
    private final TIntIntHashMap inner;

    public TroveIntIntMap(TIntIntHashMap inner) {
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
    public boolean containsKey(@Nonnull Object key) {
      return inner.containsKey((Integer) key);
    }

    @Override
    public boolean containsValue(@Nonnull Object value) {
      return inner.containsValue((Integer) value);
    }

    @Override
    public Integer get(@Nonnull Object key) {
      return inner.get((Integer) key);
    }

    @Override
    public Integer put(@Nonnull Integer key, @Nonnull Integer value) {
      return inner.put(key, value);
    }

    @Override
    public Integer remove(Object key) {
      return inner.remove((Integer) key);
    }

    @Override
    public void putAll(@Nonnull Map<? extends Integer, ? extends Integer> m) {
      inner.putAll(m);
    }

    @Override
    public void clear() {
      inner.clear();
    }

    @Nonnull
    @Override
    public Set<Integer> keySet() {
      return new TroveIntSet(inner.keySet());
    }

    @Nonnull
    @Override
    public Collection<Integer> values() {
      return new TroveIntCollection(inner.valueCollection());
    }

    @Nonnull
    @Override
    public Set<Entry<Integer, Integer>> entrySet() {
      HashSet<Entry<Integer, Integer>> entries = new HashSet<>();
      inner.forEachEntry((k, v) -> {
        entries.add(new AbstractMap.SimpleImmutableEntry<>(k, v));
        return true;
      });
      return Collections.unmodifiableSet(entries);
    }
  }

  public static class TroveIntCollection extends AbstractCollection<Integer> {
    private final TIntCollection inner;

    public TroveIntCollection(TIntCollection inner) {
      this.inner = inner;
    }

    @Nonnull
    @Override
    public Iterator<Integer> iterator() {
      TIntIterator iter = inner.iterator();
      return new Iterator<Integer>() {
        @Override
        public boolean hasNext() {
          return iter.hasNext();
        }

        @Override
        public Integer next() {
          return iter.next();
        }

        @Override
        public void remove() {
          iter.remove();
        }
      };
    }

    @Override
    public int size() {
      return inner.size();
    }
  }

  public static class TroveIntSet extends AbstractCollection<Integer> implements Set<Integer> {
    private final TIntSet inner;

    public TroveIntSet(TIntSet inner) {
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
    public boolean contains(Object o) {
      return inner.contains((Integer) o);
    }

    @Nonnull
    @Override
    public Iterator<Integer> iterator() {
      TIntIterator iter = inner.iterator();
      return new Iterator<Integer>() {
        @Override
        public boolean hasNext() {
          return iter.hasNext();
        }

        @Override
        public Integer next() {
          return iter.next();
        }
      };
    }

    @Override
    public boolean add(Integer integer) {
      return inner.add(integer);
    }

    @Override
    public boolean remove(Object o) {
      return inner.remove((Integer) o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      return inner.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
      return inner.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      return inner.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      return inner.removeAll(c);
    }

    @Override
    public void clear() {
      inner.clear();
    }
  }

  public static class TroveLongSet extends AbstractCollection<Long> implements Set<Long> {
    private final TLongSet inner;

    public TroveLongSet(TLongSet inner) {
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
    public boolean contains(Object o) {
      return inner.contains((Long) o);
    }

    @Nonnull
    @Override
    public Iterator<Long> iterator() {
      TLongIterator iter = inner.iterator();
      return new Iterator<Long>() {
        @Override
        public boolean hasNext() {
          return iter.hasNext();
        }

        @Override
        public Long next() {
          return iter.next();
        }
      };
    }

    @Override
    public boolean add(Long x) {
      return inner.add(x);
    }

    @Override
    public boolean remove(Object o) {
      return inner.remove((Long) o);
    }

    @Override
    public boolean containsAll(@Nonnull Collection<?> c) {
      return inner.containsAll(c);
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends Long> c) {
      return inner.addAll(c);
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
      return inner.retainAll(c);
    }

    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
      return inner.removeAll(c);
    }

    @Override
    public void clear() {
      inner.clear();
    }
  }

  public static class TroveLongIntMap implements Map<Long, Integer> {
    private final TLongIntHashMap inner;

    public TroveLongIntMap(TLongIntHashMap inner) {
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
    public boolean containsKey(Object key) {
      return inner.containsKey((Long) key);
    }

    @Override
    public boolean containsValue(Object value) {
      return inner.containsValue((Integer) value);
    }

    @Override
    public Integer get(Object key) {
      return inner.get((Long) key);
    }

    @Override
    public Integer put(Long key, Integer value) {
      return inner.put(key, value);
    }

    @Override
    public Integer remove(Object key) {
      return inner.remove((Long) key);
    }

    @Override
    public void putAll(@Nonnull Map<? extends Long, ? extends Integer> m) {
      inner.putAll(m);
    }

    @Override
    public void clear() {
      inner.clear();
    }

    @Nonnull
    @Override
    public Set<Long> keySet() {
      return new TroveLongSet(inner.keySet());
    }

    @Nonnull
    @Override
    public Collection<Integer> values() {
      return new TroveIntCollection(inner.valueCollection());
    }

    @Nonnull
    @Override
    public Set<Entry<Long, Integer>> entrySet() {
      HashSet<Entry<Long, Integer>> entries = new HashSet<>();
      inner.forEachEntry((k, v) -> {
        entries.add(new AbstractMap.SimpleImmutableEntry<>(k, v));
        return true;
      });
      return Collections.unmodifiableSet(entries);
    }
  }

  public static class TroveLongObjectMap<V> implements Map<Long, V> {
    private final TLongObjectHashMap<V> inner;

    public TroveLongObjectMap(TLongObjectHashMap<V> inner) {
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
    public boolean containsKey(Object key) {
      return inner.containsKey((Long) key);
    }

    @Override
    public boolean containsValue(Object value) {
      return inner.containsValue(value);
    }

    @Override
    public V get(Object key) {
      return inner.get((Long) key);
    }

    @Override
    public V put(Long key, V value) {
      return inner.put(key, value);
    }

    @Override
    public V remove(Object key) {
      return inner.remove((Long) key);
    }

    @Override
    public void putAll(@Nonnull Map<? extends Long, ? extends V> m) {
      inner.putAll(m);
    }

    @Override
    public void clear() {
      inner.clear();
    }

    @Nonnull
    @Override
    public Set<Long> keySet() {
      return new TroveLongSet(inner.keySet());
    }

    @Nonnull
    @Override
    public Collection<V> values() {
      return inner.valueCollection();
    }

    @Nonnull
    @Override
    public Set<Entry<Long, V>> entrySet() {
      return new AbstractSet<Entry<Long,V>>() {
        @Nonnull
        @Override
        public Iterator<Entry<Long, V>> iterator() {
          TLongObjectIterator<V> iter = inner.iterator();
          return new Iterator<Entry<Long, V>>() {
            @Override
            public boolean hasNext() {
              return iter.hasNext();
            }

            @Override
            public Entry<Long, V> next() {
              Entry<Long,V> e = new AbstractMap.SimpleImmutableEntry<>(iter.key(), iter.value());
              iter.advance();
              return e;
            }
          };
        }

        @Override
        public int size() {
          return inner.size();
        }
      };
    }
  }

}
