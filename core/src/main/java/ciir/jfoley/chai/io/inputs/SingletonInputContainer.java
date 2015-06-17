package ciir.jfoley.chai.io.inputs;

import java.io.IOException;
import java.util.Collections;

/**
 * @author jfoley
 */
public class SingletonInputContainer implements InputContainer {
  public final InputStreamable input;

  public SingletonInputContainer(InputStreamable input) {
    this.input = input;
  }

  @Override
  public Iterable<? extends InputStreamable> getInputs() {
    return Collections.singleton(input);
  }

  @Override
  public void close() throws IOException {
    // Nothing here.
  }
}
