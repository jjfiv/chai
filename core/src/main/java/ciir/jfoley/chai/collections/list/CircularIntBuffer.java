package ciir.jfoley.chai.collections.list;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;

/**
 * @author jfoley
 */
public class CircularIntBuffer extends AChaiList<Integer> {
  private int fill;
  private int count;
  private final int size;
  final int[] data;

  public CircularIntBuffer(int size) {
    this.data = new int[size];
    this.size = size;
    clear();
  }

  @Override
  public void clear() {
    this.fill = 0;
    this.count = 0;
  }

  @Nonnull
  @Override
  public Integer get(int i) {
     return getQuick(i);
  }

  public int getQuick(int i) {
    if(i > count) throw new NoSuchElementException();
    // read pointer is either the start, if not filled, or the fill pointer
    int read = 0;
    if(count == size) {
      read = fill;
    }
    int pos = (read + i) % size;

    return data[pos];
  }

  @Override
  public int size() {
    return count;
  }

  @Override
  public boolean add(@Nonnull Integer item) {
    push(item);
    return true;
  }
  public void push(int item) {
    count = Math.min(size, count + 1);
    data[fill++] = item;
    fill %= size; // and wrap around if necessary:
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
      hashCode = 31*hashCode + data[pos];
    }
    return hashCode;
  }

  /**
   * Push item into this collection and return the item that was pushed out, or null if none.
   * @param item the item to add to the collection.
   * @return the item that fell out
   */
  public int replace(int item) {
    count = Math.min(size, count+1);
    int pos = fill++;
    int prev = data[pos];
    data[pos] = item;
    fill %= size;
    return prev;
  }

  public boolean full() {
    return count == size;
  }
}
