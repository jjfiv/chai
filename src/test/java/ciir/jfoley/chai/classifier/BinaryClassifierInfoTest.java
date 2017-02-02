package ciir.jfoley.chai.classifier;

import ciir.jfoley.chai.collections.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class BinaryClassifierInfoTest {
  @Test
  public void testPrecisionRecallF1() {
    BinaryClassifierInfo info = new BinaryClassifierInfo();

    int fp = 3;
    int tp = 7;
    int fn = 4;
    int tn = 20;

    for (int i = 0; i < fp; i++) {
      info.update(true, false);
    }
    for (int i = 0; i < tp; i++) {
      info.update(true, true);
    }
    for (int i = 0; i < fn; i++) {
      info.update(false, true);
    }
    for (int i = 0; i < tn; i++) {
      info.update(false, false);
    }

    double precision = tp / (double) (tp + fp);
    double recall = tp / (double) (tp + fn);
    // https://en.wikipedia.org/wiki/Precision_and_recall#F-measure
    double f1 = (2.0*tp) / (double) (2*tp + fp + fn);

    assertEquals(precision, info.getPositivePrecision(), 1e-7);
    assertEquals(recall, info.getPositiveRecall(), 1e-7);
    assertEquals(f1, info.getPositiveF1(), 1e-7);
  }

  @Test
  public void testPrecisionRecallF1Bulk() {
    BinaryClassifierInfo info = new BinaryClassifierInfo();

    int fp = 3;
    int tp = 7;
    int fn = 4;
    int tn = 20;
    List<Pair<Boolean, Double>> bulk = new ArrayList<>();

    for (int i = 0; i < fp; i++) {
      bulk.add(Pair.of(false, 1.0));
      info.update(true, false);
    }
    for (int i = 0; i < tp; i++) {
      bulk.add(Pair.of(true, 1.0));
      info.update(true, true);
    }
    for (int i = 0; i < fn; i++) {
      bulk.add(Pair.of(true, -1.0));
      info.update(false, true);
    }
    for (int i = 0; i < tn; i++) {
      bulk.add(Pair.of(false, -1.0));
      info.update(false, false);
    }

    double precision = tp / (double) (tp + fp);
    double recall = tp / (double) (tp + fn);
    // https://en.wikipedia.org/wiki/Precision_and_recall#F-measure
    double f1 = (2.0*tp) / (double) (2*tp + fp + fn);

    assertEquals(precision, info.getPositivePrecision(), 1e-7);
    assertEquals(recall, info.getPositiveRecall(), 1e-7);
    assertEquals(f1, info.getPositiveF1(), 1e-7);

    BinaryClassifierInfo bulkInfo = new BinaryClassifierInfo();
    bulkInfo.update(bulk, 0.0);

    assertEquals(bulkInfo.getPositiveF1(), info.getPositiveF1(), 1e-7);
    assertEquals(bulkInfo.getAccuracy(), info.getAccuracy(), 1e-7);
    assertEquals(bulkInfo.getPositiveRecall(), info.getPositiveRecall(), 1e-7);
    assertEquals(bulkInfo.getPositivePrecision(), info.getPositivePrecision(), 1e-7);
  }

}