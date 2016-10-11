package ciir.jfoley.chai.io.inputs;

import java.io.IOException;
import java.util.Collections;

/**
 * @author jfoley
 */
public class SingletonInputContainer implements InputContainer {
  public final InputStreamable input;
  private final String name;

  public SingletonInputContainer(String name, InputStreamable input) {
    this.name = name;
    this.input = input;
  }

  @Override
  public Iterable<? extends InputStreamable> getInputs() {
    return Collections.singleton(input);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isParallel() {
    return true;
  }

  @Override
  public long estimateCount() {
    return 1;
  }

  @Override
  public void close() throws IOException {
    // Nothing here.
  }
}
