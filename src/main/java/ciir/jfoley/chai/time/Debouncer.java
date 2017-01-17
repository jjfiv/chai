package ciir.jfoley.chai.time;

import java.io.PrintStream;
import java.time.Duration;
import java.util.List;
import java.util.function.ObjIntConsumer;

/**
 * @author jfoley
 */
public class Debouncer {
  long startTime;
  long delay;
  long lastTime;
  /**
   * @param milliseconds the minimum number of milliseconds between actions.
   */
  public Debouncer(long milliseconds) {
    this.delay = milliseconds;
    this.startTime = System.currentTimeMillis();
    this.lastTime = startTime - delay;
  }
  public Debouncer() {
    this(1000);
  }

  public boolean ready() {
    long now = System.currentTimeMillis();
    if(now - lastTime >= delay) {
      lastTime = now;
      return true;
    }
    return false;
  }

  public RateEstimate estimate(long currentItem, long totalItems) {
    return new RateEstimate(System.currentTimeMillis() - startTime, currentItem, totalItems);
  }

  public String estimate(double count) {
    long timeDelta = System.currentTimeMillis() - startTime;
    double rate = count / (double) timeDelta;
    return String.format("%d items, %4.1f items/s, [%s spent].", (long) count, rate*1000.0, Debouncer.prettyTimeOfMillis(timeDelta));
  }

  public String estimateStr(long processed, long total) {
    if(total <= 0) return estimate(processed);
    return estimate(processed, total).toString();
  }

  public static String prettyTimeOfMillis(long millis) {
    StringBuilder msg = new StringBuilder();
    Duration dur = Duration.ofMillis(millis);
    long days = dur.toDays();
    if(days > 0) {
      dur = dur.minusDays(days);
      msg.append(days).append(" days");
    }
    long hours = dur.toHours();
    if(hours > 0) {
      dur = dur.minusHours(hours);
      if(msg.length() > 0) msg.append(", ");
      msg.append(hours).append(" hours");
    }
    long minutes = dur.toMinutes();
    if(minutes > 0) {
      if(msg.length() > 0) msg.append(", ");
      msg.append(minutes).append(" minutes");
    }
    dur = dur.minusMinutes(minutes);
    double seconds = dur.toMillis() / 1e3;
    if(msg.length() > 0) msg.append(", ");
    msg.append(String.format("%1.3f", seconds)).append(" seconds");
    return msg.toString();
  }

  public static <T> void forEach(PrintStream out, List<T> items, ObjIntConsumer<T> fn) {
    int total = items.size();
    Debouncer msg = new Debouncer();
    for (int i = 0; i < total; i++) {
      fn.accept(items.get(i), i);
      if(msg.ready()) {
        out.println("-> "+msg.estimate(i, total));
      }
    }
    out.println("<> "+msg.estimate(total, total));
  }

  public static class RateEstimate {
    /** time spent on job (s) */
    private final long time;
    /** fraction of job complete */
    private final double fraction;
    /** items/ms */
    private final double rate;
    /** estimated time remaining (ms) */
    private final double remaining;
    private final long itemsComplete;
    private final long totalItems;

    public RateEstimate(long timeDelta, long itemsComplete, long totalItems) {
      this.time = timeDelta;
      this.itemsComplete = itemsComplete;
      this.totalItems = totalItems;
      this.fraction = itemsComplete / (double) totalItems;
      this.rate = itemsComplete / (double) timeDelta;
      this.remaining = (totalItems - itemsComplete) / rate;
    }

    public double secondsInvested() { return time / 1000.0; }
    public double secondsLeft() {
      return remaining / 1000.0;
    }
    public double itemsPerSecond() {
      return rate * 1000.0;
    }
    public double percentComplete() {
      return fraction * 100.0;
    }

    public String toString() {
      return String.format("%d/%d items, %4.1f items/s [%s left]; [%s spent], %2.1f%% complete.", itemsComplete, totalItems, itemsPerSecond(), Debouncer.prettyTimeOfMillis((long) remaining), Debouncer.prettyTimeOfMillis(time), percentComplete());
    }
  }
}
