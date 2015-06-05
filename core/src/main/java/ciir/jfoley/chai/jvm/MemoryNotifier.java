package ciir.jfoley.chai.jvm;

import ciir.jfoley.chai.collections.util.ListFns;
import ciir.jfoley.chai.fn.GenerateFn;
import ciir.jfoley.chai.fn.TransformFn;
import ciir.jfoley.chai.lang.ThreadsafeLazyPtr;
import ciir.jfoley.chai.lang.annotations.Beta;

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
import java.util.logging.Logger;

/**
 * This class keeps a list of "Flushable" objects that it flushes when memory gets tight.
 * @author jfoley.
 */
@Beta
public class MemoryNotifier implements NotificationListener {
  private static final Logger logger = Logger.getLogger(MemoryNotifier.class.getName());
  private static final double MemoryFullFraction = 0.75;
  public static final ThreadsafeLazyPtr<MemoryNotifier> instance = new ThreadsafeLazyPtr<>(new GenerateFn<MemoryNotifier>() {
    @Override
    public MemoryNotifier get() {
      return new MemoryNotifier();
    }
  });
  private final List<Flushable> listeners;
  public MemoryNotifier() {
    this.listeners = new ArrayList<>();
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

  /**
   * Registers something that can be flushed when memory is low.
   * @param x something that implements {@link Flushable}
   */
  public static void register(Flushable x) {
    synchronized (instance) {
      instance.get().listeners.add(x);
    }
  }

  public static void unregister(Flushable x) {
    synchronized (instance) {
      instance.get().listeners.remove(x);
    }
  }

  @Override
  public void handleNotification(Notification notification, Object handback) {
    if(!notification.getType().equals(MemoryNotificationInfo.MEMORY_THRESHOLD_EXCEEDED)) return;
    logger.info("MemoryNotifier::activate");

    //--- copy the list of listener's  in a thread-safe way; doesn't matter if we get an update while we're notifying this way.
    final List<Flushable> toNotify = new ArrayList<>();
    synchronized (instance) {
      toNotify.addAll(listeners);
    }

    //--- spawn a thread to handle flushing; don't do multiple because threads can be heavy on the memory usage.
    Thread deferred = new Thread() {
      public void run() {
        for (final Flushable flushable : toNotify) {
          try {
            flushable.flush();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    };
    deferred.start();
  }

  /**
   * Some heuristics taken from Galago for finding the heap we want to monitor.
   * @return the {@link MemoryPoolMXBean} representing the heap.
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
