package ciir.jfoley.chai.random;

import ciir.jfoley.chai.collections.Pair;
import ciir.jfoley.chai.collections.TopKHeap;
import ciir.jfoley.chai.collections.util.Comparing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author jfoley
 */
public class Sample {
  /** Naive sampling: fill a heap with randomly weighted elements as you go... */
  public static <T> List<T> byRandomWeight(Iterable<T> source, int count, Random rand) {
    TopKHeap<Pair<T, Integer>> heap = TopKHeap.maxItems(count, Pair.<T,Integer>cmpRight());
    for(T newObj : source) {
      int weight = rand.nextInt();
      heap.offer(Pair.of(newObj, weight));
    }
    ArrayList<T> output = new ArrayList<>(count);
    for (Pair<T,Integer> rw : heap.getUnsortedList()) {
      output.add(rw.left);
    }
    return output;
  }
  public static <T> List<T> byRandomWeight(Iterable<T> source, int count) {
    return byRandomWeight(source, count, new Random());
  }

  public static <T> T once(List<T> input) {
    return once(input, new Random());
  }
  public static <T> T once(List<T> input, Random rand) {
    return input.get(Math.abs(rand.nextInt()) % input.size());
  }
}
