package ciir.jfoley.chai.text;

import gnu.trove.map.hash.TObjectDoubleHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.IOException;
import java.util.Set;

/**
 * @author jfoley
 */
public class HashMapLanguageModel implements LanguageModel.Builder {
  TObjectIntHashMap<String> counts;
  int length;

  public HashMapLanguageModel() {
    length = 0;
    counts = new TObjectIntHashMap<>();
  }

  public HashMapLanguageModel(TObjectIntHashMap<String> counts) {
    int[] vals = counts.values();
    length = 0;
    for (int val : vals) {
      length += val;
    }
    this.counts = counts;
  }

  @Override
  public double getTermWeight(String term) {
    return counts.get(term);
  }

  @Override
  public double getTotalWeight() {
    return length;
  }

  @Override
  public void close() throws IOException {
    counts = null;
  }

  @Override
  public void increment(String term, int amount) {
    counts.adjustOrPutValue(term, amount, amount);
    length += amount;
  }

  @Override
  public void increment(Builder clm) {
    length += clm.getTotalWeight();
    clm.getCounts().forEachEntry((key, val) -> {
      counts.adjustOrPutValue(key, val, val);
      return true;
    });
  }

  @Override
  public TObjectIntHashMap<String> getCounts() {
    return counts;
  }

  @Override
  public Set<String> getVocabulary() {
    return counts.keySet();
  }

  public void increment(TObjectDoubleHashMap<String> counts) {
    counts.forEachEntry((key, val) -> {
      length += val;
      counts.adjustOrPutValue(key, val, val);
      return true;
    });
  }
}
