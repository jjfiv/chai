package ciir.jfoley.chai.collections.util;

import java.util.Comparator;
import java.util.List;

/**
 * @author jfoley.
 */
public class QuickSort {

  static <T> void swap(List<T> data, int i, int j) {
    T oi = data.get(i);
    T oj = data.get(j);
    data.set(j, oi);
    data.set(i, oj);
  }

  /**
   * A version of sort that doesn't make a bunch of extra copies of the collection.
   * Yep, totally based on Wikipedia.
   * @param <T> the type of the objects to compare.
   * @param cmp the method of comparing objects.
   * @param data the data to sort.
   * @param left the start index to sort.
   * @param right the end index to sort.
   */
  static <T> void sort(Comparator<T> cmp, List<T> data, int left, int right) {
    if(left >= right) return;

    // Partition
    final int pivotCandidate = (left+right)/2;
    // move pivot to rightmost
    swap(data, pivotCandidate, right);
    // Determine pivot location while putting values in the correct half.
    int pivotIndex = left;
    T robj = data.get(right);
    for(int i=left; i<right; i++) {
      if(cmp.compare(data.get(i), robj) < 0) {
        swap(data, i, pivotIndex);
        pivotIndex++;
      }
    }

    // Now move pivot to where it really belongs.
    swap(data, pivotIndex, right);

    // recurse left & right
    sort(cmp, data, left, pivotIndex-1);
    sort(cmp, data, pivotIndex+1, right);
  }

  /**
   * QuickSort a list without making any copies of the damn thing. Looking at you, Collections.sort.
   * @param cmp compare
   * @param data the list to sort.
   * @param <T> the type of object within the list.
   */
  public static <T> void sort(Comparator<T> cmp, List<T> data) {
    sort(cmp, data, 0, data.size()-1);
  }

  /**
   * QuickSort a list without making any copies of the damn thing. Looking at you, Collections.sort.
   * @param data the list to sort.
   * @param <T> the type of object within the list.
   */
  public static <T> void sort(List<T> data) {
    sort(Comparing.<T>defaultComparator(), data, 0, data.size()-1);
  }
}
