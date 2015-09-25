package ciir.jfoley.chai.collections.maps;

import gnu.trove.map.hash.TObjectIntHashMap;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * @author jfoley.
 */
public class StringIntHashMap {
  private final int numBuckets;
  public ArrayList<TObjectIntHashMap<String>> vocabulary;
  public int nextId;

  public StringIntHashMap(int sizeBuckets) {
    this.numBuckets = sizeBuckets;
    vocabulary = new ArrayList<>(sizeBuckets);
    for (int i = 0; i < sizeBuckets; i++) {
      vocabulary.add(new TObjectIntHashMap<>());
    }
    nextId = 1; //new AtomicInteger(1);
  }

  TObjectIntHashMap<String> getBucket(String input) {
    // choose a bucket based on the string length:
    int bucket = Math.min(input.length()-1, numBuckets-1);
    return vocabulary.get(bucket);
  }

  public int getOrAssign(String input) {
    TObjectIntHashMap<String> bucket = getBucket(input);
    int id = bucket.get(input);
    if(id == bucket.getNoEntryValue()) {
      id = nextId++;
      bucket.put(input, id);
    }
    return id;
  }
  public int getOrAssign(String input, IntConsumer onNewIdFn) {
    TObjectIntHashMap<String> bucket = getBucket(input);
    int id = bucket.get(input);
    if(id == bucket.getNoEntryValue()) {
      id = nextId++;
      onNewIdFn.accept(id);
      bucket.put(input, id);
    }
    return id;
  }

  public void forEachEntry(Consumer<Map.Entry<String,Integer>> onEntryFn) {
    for (TObjectIntHashMap<String> map : vocabulary) {
      //for (Map.Entry<String, Integer> item : map.entrySet()) {
        //onEntryFn.accept(item);
      //}
    }
  }
}
