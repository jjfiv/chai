package ciir.jfoley.chai.fn;

import ciir.jfoley.chai.errors.FatalError;

/**
 * @author jfoley
 */
public abstract class ESinkFn<T> implements SinkFn<T> {
  @Override
  public void process(T input) {
    try {
      processE(input);
    } catch (Exception e) {
      throw new FatalError(e);
    }
  }

  protected abstract void processE(T input) throws Exception;
}
