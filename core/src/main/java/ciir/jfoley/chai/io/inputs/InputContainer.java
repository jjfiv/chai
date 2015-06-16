package ciir.jfoley.chai.io.inputs;

import java.io.Closeable;

/**
 * @author jfoley
 */
public interface InputContainer extends Closeable {
  Iterable<? extends InputStreamable> getInputs();
}
