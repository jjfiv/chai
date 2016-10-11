package ciir.jfoley.chai.collections.maps;

import ciir.jfoley.chai.collections.util.ListFns;
import ciir.jfoley.chai.math.StreamingStats;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley.
 */
public class StringIntHashMapTest {

  @Test
  public void speedTest1() {
    int N = 1000;

    StreamingStats stats2 = new StreamingStats();
    ConcurrentHashMap<String,Integer> vocab2 = new ConcurrentHashMap<>();
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < i; j++) {
        long start = System.nanoTime();
        String item = Integer.toString(j);
        long end = System.nanoTime();
        stats2.push((end-start)/1e9);
        int id = vocab2.computeIfAbsent(item, (ignored) -> vocab2.size() + 1);
        assertEquals(j+1, id);
      }
    }

    System.err.println(stats2);
    StreamingStats stats = new StreamingStats();
    StringIntHashMap vocab = new StringIntHashMap(5);
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < i; j++) {
        long start = System.nanoTime();
        String item = Integer.toString(j);
        long end = System.nanoTime();
        stats.push((end-start)/1e9);
        int id = vocab.getOrAssign(item);
        assertEquals(j+1, id);
      }
    }

    System.err.println(stats);
    System.err.println(ListFns.map(vocab.vocabulary, TObjectIntHashMap::size));

  }
}