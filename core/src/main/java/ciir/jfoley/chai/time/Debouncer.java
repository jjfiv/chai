package ciir.jfoley.chai.time;

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
    double timeDelta = System.currentTimeMillis() - startTime;
    double rate = count / timeDelta;
    return String.format("%4.1f items/s; total_time=%4.1fs", rate * 1000.0, timeDelta / 1000.0);
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

    public RateEstimate(long timeDelta, long itemsComplete, long totalItems) {
      this.time = timeDelta;
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
      return String.format("%4.1f items/s %4.1f seconds left; %4.1f seconds spent, %2.1f%% complete.", itemsPerSecond(), secondsLeft(), secondsInvested(), percentComplete());
    }
  }
}
