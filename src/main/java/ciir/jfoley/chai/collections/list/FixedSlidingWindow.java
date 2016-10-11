package ciir.jfoley.chai.collections.list;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author jfoley
 */
public class FixedSlidingWindow<T> extends AChaiList<T> {
  private int fill;
  private int count;
  private final int size;
  ArrayList<T> data;

  public FixedSlidingWindow(int size) {
    this.data = new ArrayList<>(size);
    this.size = size;
    clear();
  }

  @Override
  public void clear() {
    for (int i = data.size(); i < size; i++) {
      data.add(null);
    }
    this.fill = 0;
    this.count = 0;
  }

  @Override
  public T get(int i) {
    if(i > count) throw new NoSuchElementException();
    // read pointer is either the start, if not filled, or the fill pointer
    int read = 0;
    if(count == size) {
      read = fill;
    }
    int pos = (read + i) % size;

    return data.get(pos);
  }

  @Override
  public int size() {
    return count;
  }

  @Override
  public boolean add(T item) {
    count = Math.min(size, count+1);
    data.set(fill++, item);
    fill %= size; // and wrap around if necessary:
    return true;
  }

  @Override
  public int hashCode() {
    // read pointer is either the start, if not filled, or the fill pointer
    int read = 0;
    if(count == size) {
      read = fill;
    }
    int hashCode = 1;
    for (int i = 0; i < count; i++) {
      int pos = (read + i) % size;
      T e = data.get(pos);
      hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
    }
    return hashCode;
  }

  @Nonnull
  @Override
  public Iterator<T> iterator() {
    int read = (count == size) ? fill : 0;
    return new Iterator<T>() {
      int i=0;
      @Override
      public boolean hasNext() {
        return i<count;
      }

      @Override
      public T next() {
        i++;
        return data.get((read+i) % size);
      }
    };
  }

  /**
   * Push item into this collection and return the item that was pushed out, or null if none.
   * @param item the item to add to the collection.
   * @return the item that fell out
   */
  public T replace(T item) {
    count = Math.min(size, count+1);
    int pos = fill++;
    T prev = data.get(pos);
    data.set(pos, item);
    fill %= size;
    return prev;
  }

  public boolean full() {
    return count == size;
  }
}
