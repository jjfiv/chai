package ciir.jfoley.chai.io.archive;

import ciir.jfoley.chai.collections.util.IterableFns;
import ciir.jfoley.chai.fn.PredicateFn;
import ciir.jfoley.chai.io.inputs.InputContainer;

/**
 * @author jfoley
 */
public abstract class Archive<T extends ArchiveEntry> implements InputContainer {
  public Iterable<T> listFileEntries() {
    return IterableFns.filter(listEntries(), input -> !input.isDirectory());
  }

  public Iterable<T> getInputs() { return listFileEntries(); }

  public abstract Iterable<T> listEntries();
}
