package ciir.jfoley.chai.text;

import org.lemurproject.galago.utility.MathUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for computing the Relevance Model.
 * @author jfoley
 */
public class RelevanceModel {
  // Implementation here is identical to the Relevance Model unigram normaliztion in Indri.
  // See RelevanceModel.cpp for details
  public static <T> Map<T, Double> logsToPosteriors(Map<T, ? extends Number> results) {
    Map<T, Double> scores = new HashMap<T, Double>();
    if (results.isEmpty()) {
      return scores;
    }

    int i=0;
    double[] values = new double[results.size()];
    for (Number score : results.values()) {
      values[i++] = score.doubleValue();
    }

    // compute the denominator
    double logSumExp = MathUtils.logSumExp(values);

    results.forEach((k, v) -> {
      double logPosterior = v.doubleValue() - logSumExp;
      scores.put(k, Math.exp(logPosterior));
    });

    return scores;
  }
}
