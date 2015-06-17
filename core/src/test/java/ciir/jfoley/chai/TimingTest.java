package ciir.jfoley.chai;

import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

/**
 * @author jfoley
 */
public class TimingTest {
  private final static Logger logger = Logger.getLogger(TimingTest.class.getName());
  @Test
  public void sillyTest() {
    final long attemptedTime = 40;
    long timeSpent = Timing.milliseconds((Runnable) () -> {
      while(true) {
        try {
          Thread.sleep(attemptedTime);
          break;
        } catch (InterruptedException e) {
          // try to sleep again.
        }
      }
    });

    logger.fine("Tried to sleep for: "+attemptedTime+" actual time: "+timeSpent);
    // very weak guarantees on timing with Thread.sleep and the like:
    assertTrue(timeSpent >= attemptedTime);
  }

}