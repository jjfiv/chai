package ciir.jfoley.chai.jvm;

import ciir.jfoley.chai.collections.util.ListFns;
import ciir.jfoley.chai.fn.GenerateFn;
import ciir.jfoley.chai.fn.TransformFn;
import ciir.jfoley.chai.lang.ThreadsafeLazyPtr;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import java.io.Flushable;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryNotificationInfo;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * @author jfoley.
 */
public class MemoryNotifier implements NotificationListener {
  private static final Logger logger = Logger.getLogger(MemoryNotifier.class.getName());
  private static final double MemoryFullFraction = 0.75;
  public static ThreadsafeLazyPtr<MemoryNotifier> instance = new ThreadsafeLazyPtr<>(new GenerateFn<MemoryNotifier>() {
    @Override
    public MemoryNotifier get() {
      return new MemoryNotifier();
    }
  });
  private final Vector<Flushable> flushables;
  public MemoryNotifier() {
    this.flushables = new Vector<>();
    MemoryPoolMXBean heap = findHeap();

    if(heap != null) {
      // set usage threshold on the heap
      heap.setUsageThreshold((long) (heap.getUsage().getMax() * MemoryFullFraction));
      // add listener to bean:
      ((NotificationEmitter) ManagementFactory.getMemoryMXBean()).addNotificationListener(this, null, null);
    } else {
      logger.warning("MemoryMonitoringNotSupported!");
    }
  }

  public static void register(Flushable x) {
    instance.get().flushables.add(x);
  }

  public static void unregister(Flushable x) {
    instance.get().flushables.remove(x);
  }

  @Override
  public void handleNotification(Notification notification, Object handback) {
    if(!notification.getType().equals(MemoryNotificationInfo.MEMORY_THRESHOLD_EXCEEDED)) return;

    for (final Flushable flushable : flushables) {
      Thread deferred = new Thread() {
        public void run() {
          try {
            flushable.flush();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      };
      deferred.start();
    }
  }

  /**
   * Some heuristics taken from Galago for finding the heap we want to monitor.
   * @return the MXBean representing the heap.
   */
  public static MemoryPoolMXBean findHeap() {
    List<MemoryPoolMXBean> heaps = new ArrayList<>();
    for (MemoryPoolMXBean mxBean : ManagementFactory.getMemoryPoolMXBeans()) {
      if(mxBean.getType() != MemoryType.HEAP) {
        continue;
      }
      if(!mxBean.isUsageThresholdSupported())
        continue;

      heaps.add(mxBean);
    }

    return ListFns.maxBy(heaps, new TransformFn<MemoryPoolMXBean, Long>() {
      @Override
      public Long transform(MemoryPoolMXBean input) {
        return input.getUsage().getMax();
      }
    });
  }

}
