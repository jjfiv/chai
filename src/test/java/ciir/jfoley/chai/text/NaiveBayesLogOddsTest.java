package ciir.jfoley.chai.text;

import ciir.jfoley.chai.classifier.AUC;
import ciir.jfoley.chai.collections.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class NaiveBayesLogOddsTest {
  List<String> tok(String input) {
    return Arrays.asList(input.toLowerCase().split("\\s+"));
  }
  @Test
  public void testHappySad() {
    HashMapLanguageModel posLM = new HashMapLanguageModel();
    HashMapLanguageModel bgLM = new HashMapLanguageModel();

    String happy = "good happy smile awesome";

    for (String term : tok(happy)) {
      posLM.increment(term);
      bgLM.increment(term);
    }

    List<Pair<List<String>, Integer>> stuff = Arrays.asList(
        Pair.of(tok("this is a happy meal"), 1),
        Pair.of(tok("this is a good"), 1),
        Pair.of(tok("everything is awesome"), 1),
        Pair.of(tok("smile is as smile does"), 1),
        Pair.of(tok("this is a sad meal"), 0),
        Pair.of(tok("this is a bad"), 0),
        Pair.of(tok("sad is as sad does"), 0),
        Pair.of(tok("now just many words for the negative model so that relative probabilities get quites mall in comparison I don't know what will happen in general but if we keep adding words here then it will be pretty simple for things to appear sad or the other thing in comparison"), 0)
    );

    // keep working on bgLM:
    for (Pair<List<String>, Integer> pr : stuff) {
      for (String s : pr.left) {
        bgLM.increment(s);
      }
    }

    List<Pair<Boolean, Double>> preds = new ArrayList<>();
    for (Pair<List<String>, Integer> instance : stuff) {
      double score = NaiveBayesLogOdds.linearSmoothingScore(Collections.emptySet(), 0.8, posLM, bgLM, instance.left);
      preds.add(Pair.of(instance.right > 0, score));
    }
    double auc = AUC.compute(preds);
    assertEquals(1.0, AUC.compute(preds), 0.01);
    assertEquals(1.0, AUC.computeAP(preds), 0.01);
    assertEquals(1.0, AUC.computePrec(preds, 3), 0.01);

    // precision at 5 with only 4 positives is kind of sad:
    // TODO: what does trec_eval do?
    assertEquals(0.8, AUC.computePrec(preds, 5), 0.01);


  }

}