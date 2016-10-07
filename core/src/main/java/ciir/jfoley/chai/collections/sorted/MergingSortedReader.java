package ciir.jfoley.chai.collections.sorted;

import ciir.jfoley.chai.fn.SinkFn;
import ciir.jfoley.chai.io.IO;

import java.io.IOException;
import java.util.Collection;
import java.util.PriorityQueue;

/**
 * @author jfoley
 */
public class MergingSortedReader<T extends Comparable<T>> implements SortedReader<T> {
  private final PriorityQueue<SortedReader<T>> queue;

  public MergingSortedReader(Collection<SortedReader<T>> readers) {
    this.queue = new PriorityQueue<>();
    for (SortedReader<T> reader : readers) {
      queue.offer(reader);
    }
  }

  @Override
  public boolean hasNext() {
    if(queue.isEmpty()) return false;
    return queue.peek().hasNext();
  }

  @Override
  public T peek() {
    if(queue.isEmpty()) return null;
    return queue.peek().peek();
  }

  @Override
  public T next() {
    SortedReader<T> minimum = queue.poll();
    T obj = minimum.next();
    if (minimum.hasNext()) {
      queue.offer(minimum);
    } else {
      IO.close(minimum);
    }
    return obj;
  }

  @Override
  public void close() throws IOException {
    for (SortedReader<T> tRunReader : queue) {
      tRunReader.close();
    }
    queue.clear();
  }

  /**
   * This is actually more efficient than iterating, since it adds and removes from the priority queue as minimally as possible, and assumes there's order within the runs.
   * @param collector callback function that handles each item in turn.
   * @throws IOException
   */
  public void forAll(SinkFn<T> collector) throws IOException {
    while (queue.size() > 1) {
      // find minimum, pull it out:
      SortedReader<T> minimum = queue.poll();
      // go until nextBest needs to go.
      SortedReader<T> nextBest = queue.peek();

      while(minimum.hasNext() && minimum.compareTo(nextBest) <= 0) {
        collector.process(minimum.next());
      }
      if(minimum.hasNext()) {
        queue.offer(minimum);
      } else {
        minimum.close();
      }
    }

    if(queue.size() == 1) {
      SortedReader<T> last = queue.poll();
      while(last.hasNext()) {
        collector.process(last.next());
      }
      last.close();
    }
  }
}
