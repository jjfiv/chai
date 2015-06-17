package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.collections.list.AChaiList;
import ciir.jfoley.chai.fn.SinkFn;

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

  public TopKHeap(int maxSize, Comparator<? super T> cmp) {
    this.cmp = cmp;
    this.fillPtr = 0;
    this.maxSize = maxSize;
    this.data = new ArrayList<>(maxSize);
    for (int i = 0; i < maxSize; i++) {
      data.add(null);
    }
  }

  public T peek() {
    return data.get(0);
  }

  /**
   * Adds an item to the heap IFF the heaps is small OR the min-item is worse than this item.
   * @return true if the item was kept, false if it was discarded.
   */
  public boolean offer(T d) {
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

  @SuppressWarnings("NullableProblems")
  @Override
  public boolean addAll(Collection<? extends T> other) {
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


  public static <T> TopKHeap<T> maxItems(int maxSize, Comparator<? super T> cmp) {
    return new TopKHeap<>(maxSize, cmp);
  }
  public static <T> TopKHeap<T> minItems(int minSize, Comparator<? super T> cmp) {
    return new TopKHeap<>(minSize, cmp.reversed());
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
