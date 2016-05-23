package ciir.jfoley.chai.io.inputs;

import java.io.Closeable;

/**
 * @author jfoley
 */
public interface InputContainer extends Closeable {
  Iterable<? extends InputStreamable> getInputs();
  String getName();
  boolean isParallel();
  /** Returns 0 or a valid count, if possible. */
  long estimateCount();
}
