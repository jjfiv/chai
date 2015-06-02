package ciir.jfoley.chai.collections;

import java.util.*;

/**
 * For when you want the intersection or something but you don't actually care about speed. O(n) contains etc., but original add order is preserved.
 * @author jfoley
 */
@SuppressWarnings({"NullableProblems", "SuspiciousToArrayCall"})
public class ListBasedOrderedSet<T> implements Set<T> {
  public final List<T> list;

  public ListBasedOrderedSet() {
    this.list = new ArrayList<>();
  }

  /**
   * When you're constructing from an existing collection.
   * @param ts the collection to start from.
   */
  public ListBasedOrderedSet(Collection<? extends T> ts) {
    this();
    this.addAll(ts);
  }

  public List<T> toList() {
    return list;
  }

  @Override
  public Iterator<T> iterator() {
    return list.iterator();
  }

  @Override
  public Object[] toArray() {
    return list.toArray();
  }

  @Override
  public <T1> T1[] toArray(T1[] t1s) {
    return list.toArray(t1s);
  }

  @Override
  public boolean add(T pred) {
    for (T already : list) {
      if(already.equals(pred)) {
        return false;
      }
    }
    list.add(pred);
    return true;
  }

  @Override
  public boolean remove(Object o) {
    return list.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> objects) {
    return list.containsAll(objects);
  }

  @Override
  public boolean addAll(Collection<? extends T> ts) {
    boolean changed = false;
    for (T t : ts) {
      changed |= this.add(t);
    }
    return changed;
  }

  @Override
  public boolean retainAll(Collection<?> objects) {
    return list.retainAll(objects);
  }

  @Override
  public boolean removeAll(Collection<?> objects) {
    return list.removeAll(objects);
  }

  @Override
  public void clear() {
    list.clear();
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return list.contains(o);
  }
}
