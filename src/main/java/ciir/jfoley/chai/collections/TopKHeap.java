package ciir.jfoley.chai.collections;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author jfoley
 */
public class TopKHeap<T> {
  final Comparator<T> cmp;
  final ArrayList<T> data;
  int fillPtr;
  final int maxSize;

  public TopKHeap(int maxSize, Comparator<T> cmp) {
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
   * Adds an item to the heap IFF the heaps is small OR the min-item is worse than this item
   */
  public void offer(T d) {
    // if we're small
    if (fillPtr < maxSize) {
      data.set(fillPtr, d);
      fillPtr++;
      bubbleUp(fillPtr - 1);


      // or if smallest item is worse than this document
    } else if (cmp.compare(d, data.get(0)) > 0) {
      data.set(0, d);
      bubbleDown(0);
    }
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
}
