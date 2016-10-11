package ciir.jfoley.chai.time;

import ciir.jfoley.chai.collections.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jfoley
 */
public class PrettyDuration {
  public final long nanoseconds;

  private PrettyDuration(long nanoseconds) {
    this.nanoseconds = nanoseconds;
  }

  public static double NS_in_MS = 1000;
  public static double MS_in_S = 1000;
  public static double S_in_MIN = 60;
  public static double MIN_in_H = 60;
  public static double H_in_D = 24;
  public static double D_in_W = 7;



  public List<Pair<String,Integer>> byNonzeroUnit() {
    double ns = nanoseconds;
    double ms = ns / NS_in_MS;
    double s = ms / MS_in_S;
    double min = s / S_in_MIN;
    double hrs = min / MIN_in_H;
    double days = hrs / H_in_D;
    double weeks = days / D_in_W;

    List<Pair<String,Integer>> output = new ArrayList<>();
    List<Double> conversions = Arrays.asList(
        D_in_W, H_in_D, MIN_in_H, S_in_MIN, MS_in_S, NS_in_MS
    );
    List<String> units = Arrays.asList(
        "weeks", "days", "hours", "minutes", "seconds", "milliseconds", "nanoseconds"
    );
    List<Double> ordered = Arrays.asList(
        weeks,days,hrs,min,s,ms,ns
    );

    for (int i = 0; i < ordered.size(); i++) {
      double value = ordered.get(i);
      int intValue = (int) Math.floor(value);
      if(intValue == 0) continue;

      // found the first nonzero value
      output.add(Pair.of(units.get(i), intValue));
      if(i+1 < ordered.size()) {
        double remainder = value - intValue;
        remainder *= conversions.get(i);
        ordered.set(i+1, remainder);
      }
    }

    return output;
  }

  @Nonnull
  public static PrettyDuration ms(long milliseconds) {
    return new PrettyDuration(milliseconds*1000);
  }

  @Nonnull
  public static PrettyDuration ns(long nanoseconds) {
    return new PrettyDuration(nanoseconds);
  }

  @Nonnull
  public static PrettyDuration s(long seconds) {
    return new PrettyDuration(seconds * 1000 * 1000);
  }


}
