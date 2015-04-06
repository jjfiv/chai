package ciir.jfoley.chai.random;

import ciir.jfoley.chai.collections.Pair;
import ciir.jfoley.chai.collections.TopKHeap;
import ciir.jfoley.chai.collections.list.IntList;

import java.nio.charset.Charset;
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


  public static char nextASCII(Random rand) {
    return (char) rand.nextInt(128);
  }

  public static List<String> strings(Random rand, int number) {
    ArrayList<String> output = new ArrayList<>(number);
    for (int i = 0; i < number; i++) {
      // half the time, use a string over again:
      if(output.size() > 0 && rand.nextDouble() > 0.5) {
        output.add(output.get(rand.nextInt(output.size())));
      }
      // half the time, make a new string:
      int length = rand.nextInt(7)+3;
      byte[] data = new byte[length];
      rand.nextBytes(data);
      output.add(new String(data, Charset.forName("ASCII")));
    }
    return output;
  }

  public static List<Integer> randomIntegers(int N, int bound) {
    IntList output = new IntList(N);
    Random rand = new Random();
    for (int i = 0; i < N; i++) {
      output.add(rand.nextInt(bound));
    }
    return output;
  }
}
