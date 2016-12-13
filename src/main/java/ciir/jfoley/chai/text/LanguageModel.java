package ciir.jfoley.chai.text;

import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.Closeable;
import java.util.List;
import java.util.Set;

/**
 * @author jfoley
 */
public interface LanguageModel extends Closeable {
  double getTermWeight(String term);
  double getTotalWeight();


  interface Builder extends LanguageModel {
    default void increment(String term) { increment(term, 1); }
    void increment(String term, int amount);
    default void increment(Builder clm) {
      Builder that = this;
      clm.getCounts().forEachEntry((term, amount) -> {
        that.increment(term, amount);
        return true;
      });
    }
    default void increment(List<String> doc) {
      for (String term : doc) { increment(term, 1); }
    }
    TObjectIntHashMap<String> getCounts();
    Set<String> getVocabulary();
  }
}