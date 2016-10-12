package ciir.jfoley.chai.lang;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author jfoley
 */
public class ThreadsafeLazyPtrTest {
  private ThreadsafeLazyPtr<String> costlyStr = new ThreadsafeLazyPtr<>(() -> {
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "done";
  });

  @Test
  public void testThreadsafeStr() {
    Runnable job = () -> {
      Assert.assertEquals("done", costlyStr.get());
    };

    // Try to trigger a race:
    for (int i = 0; i < 100; i++) {
      ForkJoinPool.commonPool().submit(job);
    }

    // wait for jobs to finish:
    ForkJoinPool.commonPool().awaitQuiescence(1, TimeUnit.SECONDS);
  }

}