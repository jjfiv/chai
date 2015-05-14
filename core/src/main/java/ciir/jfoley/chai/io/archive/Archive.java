package ciir.jfoley.chai.io.archive;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jfoley
 */
public abstract class Archive<T extends ArchiveEntry> implements Closeable {
  public List<T> listFileEntries() {
    List<T> output = new ArrayList<>();
    for (T entry : listEntries()) {
      if (entry.isDirectory()) continue;
      output.add(entry);
    }
    return output;
  }

  public abstract List<T> listEntries();
}
