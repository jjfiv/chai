package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.list.AChaiList;
import ciir.jfoley.chai.collections.util.Comparing;
import ciir.jfoley.chai.fn.SinkFn;
import gnu.trove.map.hash.TObjectIntHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Based on Galago's implementation of a FixedSizeMinHeap by Sam Huston.
 * Edited to use an ArrayList instead, and to implement some interfaces.
 *
 * @author jfoley
 */
public class TopKHeap<T> extends AChaiList<T> implements SinkFn<T> {
  final Comparator<? super T> cmp;
  final ArrayList<T> data;
  int fillPtr;
  final int maxSize;
  long totalSeen = 0;

  /**
   * Use this class if you want an easy weighting of any T.
   * @param <T>
   */
  public static class Weighted<T> implements Comparable<Weighted<?>>, Map.Entry<T, Double> {
    public final double weight;
    public final T object;

    public Weighted(double weight, T object) {
      this.weight = weight;
      this.object = object;
    }

    @Override
    public int compareTo(@Nonnull Weighted<?> o) {
      return Double.compare(this.weight, o.weight);
    }

    public int hashCode() {
      return object.hashCode();
    }

    @Override
    public String toString() {
      return object.toString()+":"+weight;
    }

    @Override
    public T getKey() {
      return object;
    }

    @Override
    public Double getValue() {
      return weight;
    }

    @Override
    public Double setValue(Double value) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object other) {
      if(other instanceof Weighted) {
        Weighted w = (Weighted) other;
        return Objects.equals(object, w.object);
      }
      return false;
    }
  }

  public TopKHeap(int maxSize) {
    this(maxSize, Comparing.defaultComparator());
  }
  public TopKHeap(int maxSize, Comparator<? super T> cmp) {
    this.cmp = cmp;
    this.fillPtr = 0;
    this.maxSize = maxSize;
    this.data = new ArrayList<>(maxSize);
    for (int i = 0; i < maxSize; i++) {
      data.add(null);
    }
  }

  @Override
  public void clear() {
    this.fillPtr = 0;
    for (int i = 0; i < maxSize; i++) {
      data.add(null);
    }
  }

  @Nullable
  public T peek() {
    if(fillPtr > 0) {
      return data.get(0);
    }
    return null;
  }

  public long getTotalSeen() {
    return totalSeen;
  }

  /**
   * Adds an item to the heap IFF the heaps is small OR the min-item is worse than this item.
   * @return true if the item was kept, false if it was discarded.
   */
  public boolean offer(T d) {
    totalSeen++;
    if (fillPtr < maxSize) {
      // If we've not yet filled the heap, push_back.
      data.set(fillPtr, d);
      fillPtr++;
      bubbleUp(fillPtr - 1);

      return true;
    } else if (cmp.compare(d, data.get(0)) > 0) {
      // or if smallest item is worse than this document
      data.set(0, d);
      bubbleDown(0);
      return true;
    }

    // No change.
    return false;
  }

  public List<T> getSorted() {
    List<T> data = getUnsortedList();
    Collections.sort(data, Collections.reverseOrder(cmp));
    return data;
  }

  public List<T> getUnsortedList() {
    return new ArrayList<>(data.subList(0, fillPtr));
  }

  private void swap(int i, int j) {
    T p = data.get(i);
    data.set(i, data.get(j));
    data.set(j, p);
  }

  private void bubbleUp(int pos) {
    int parent = (pos - 1) / 2;
    if (cmp.compare(data.get(pos), data.get(parent)) < 0) {
      swap(pos, parent);
      bubbleUp(parent);
    }
  }

  private void bubbleDown(int pos) {
    int child1 = (2 * pos) + 1;
    int child2 = child1 + 1;

    int selectedChild = (child1 < fillPtr) ? child1 : -1;

    if (child2 < fillPtr) {
      selectedChild = (cmp.compare(data.get(child1), data.get(child2)) < 0) ? child1 : child2;
    }

    // the parent is bigger than the child (assuming a child)
    if (selectedChild > 0 && cmp.compare(data.get(pos), data.get(selectedChild)) > 0) {
      swap(selectedChild, pos);
      bubbleDown(selectedChild);
    }
  }

  @Override
  public void process(T input) {
    offer(input);
  }

  @Override
  public boolean add(T input) {
    return offer(input);
  }

  @Override
  public boolean addAll(@Nonnull Collection<? extends T> other) {
    List<T> candidates = new ArrayList<>(other);
    Collections.sort(candidates, cmp);

    // Add to heap until one of them returns false.
    boolean change = false;
    for (T candidate : candidates) {
      if(!offer(candidate)) {
        break;
      }
      change = true;
    }

    return change;
  }


  @Nonnull
  public static <T> TopKHeap<T> maxItems(int maxSize, Comparator<? super T> cmp) {
    return new TopKHeap<>(maxSize, cmp);
  }
  @Nonnull
  public static <T> TopKHeap<T> minItems(int minSize, Comparator<? super T> cmp) {
    return new TopKHeap<>(minSize, cmp.reversed());
  }

  public static <T> List<T> takeTop(int k, Iterable<T> input) {
    TopKHeap<T> top = new TopKHeap<T>(k);
    for (T t : input) {
      top.process(t);
    }
    return top.getSorted();
  }

  public static <T> List<Weighted<T>> takeTop(int k, TObjectIntHashMap<T> objects) {
    TopKHeap<Weighted<T>> top = new TopKHeap<>(k);
    objects.forEachEntry((obj, count) -> {
      top.offer(new Weighted<>(count, obj));
      return true;
    });
    return top.getSorted();
  }

  @Override
  public T get(int index) {
    return data.get(index);
  }

  @Override
  public int size() {
    return fillPtr;
  }
}
