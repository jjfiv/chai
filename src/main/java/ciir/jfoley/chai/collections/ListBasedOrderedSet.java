package ciir.jfoley.chai.collections;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * For when you want the intersection or something but you don't actually care about speed. O(n) contains etc., but original add order is preserved.
 * @author jfoley
 */
public class ListBasedOrderedSet<T> extends AbstractSet<T> {
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
  @Nonnull
  public Iterator<T> iterator() {
    return list.iterator();
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
  public boolean containsAll(@Nonnull Collection<?> objects) {
    return list.containsAll(objects);
  }

  @Override
  public boolean addAll(@Nonnull Collection<? extends T> ts) {
    boolean changed = false;
    for (T t : ts) {
      changed |= this.add(t);
    }
    return changed;
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
