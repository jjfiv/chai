package ciir.jfoley.chai.coders.files;

import ciir.jfoley.chai.IntMath;

/**
 * @author jfoley
 */
public class FileSlice {
  /**
   * inclusive
   */
  public final long start;
  /**
   * exclusive
   */
  public final long end;

  public FileSlice(long start, long end) {
    assert (end > start);
    this.start = start;
    this.end = end;
  }

  public int size() {
    return IntMath.fromLong(end - start);
  }
}
