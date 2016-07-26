package ciir.jfoley.chai.io.inputs;

import ciir.jfoley.chai.IntMath;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author jfoley
 */
public interface InputContainer extends Closeable {
  Iterable<? extends InputStreamable> getInputs();
  String getName();
  boolean isParallel();
  /** Returns 0 or a valid count, if possible. */
  long estimateCount();

  /**
   * Operates in parallel if possible.
   * @param action action to apply to contents.
   */
  default void forEach(Consumer<InputStreamable> action) {
    if(isParallel()) {
      // read into a parallelizable list:
      ArrayList<InputStreamable> items = new ArrayList<>(IntMath.fromLong(estimateCount()));
      for (InputStreamable x : getInputs()) { items.add(x); }
      // operate in parallel:
      items.parallelStream().forEach(action);
    } else {
      for (InputStreamable streamable : getInputs()) {
        action.accept(streamable);
      }
    }
  }
}
