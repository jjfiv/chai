package ciir.jfoley.chai.text;

import java.util.List;
import java.util.Set;

/**
 * @author jfoley
 */
public class NaiveBayesLogOdds {
  /**
   * Actually calculate the log-odds score for a query string.
   * @param stopwords stopwords to skip
   * @param lambda smoothing param
   * @param posLanguageModel positive language model
   * @param index background/general language model
   * @param qterms query terms to score
   * @return log odds (log P(C|d) - log P(NC | d))
   */
  protected static double linearSmoothingScore(Set<String> stopwords, double lambda, LanguageModel posLanguageModel, LanguageModel index, List<String> qterms) {
    double collectionLength = index.getTotalWeight();
    double length = posLanguageModel.getTotalWeight();

    double totalPosProb = 0;
    double bgdProb = 0;
    for (String qterm : qterms) {
      if (stopwords.contains(qterm) || qterm.length() <= 3) continue;
      double tf = posLanguageModel.getTermWeight(qterm);
      double cf = index.getTermWeight(qterm);
      if (cf == 0) continue;
      double bgProb = cf / collectionLength;
      double posProb = lambda * (tf / length) + (1 - lambda) * bgProb;
      assert (!Double.isNaN(posProb));
      assert (!Double.isNaN(bgProb));
      totalPosProb += Math.log(posProb);
      bgdProb += Math.log(bgProb);
      assert (!Double.isNaN(totalPosProb));
      assert (!Double.isNaN(bgdProb));
    }
    return (totalPosProb - bgdProb);
  }
}
