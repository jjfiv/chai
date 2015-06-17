package ciir.jfoley.chai.io.inputs;

import ciir.jfoley.chai.io.CompressionCodec;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jfoley
 */
public interface InputStreamable {
  String getName();
  InputStream getRawInputStream() throws IOException;

  /**
   * This method depending on the other two means that it handles nested decompression -- if the InputStreamable is a ArchiveEntry, you can now decode a .gz file within a zip file, for instance :)
   * @return an InputStream of the object
   * @throws IOException if there's some kind of failure along the way.
   */
  default InputStream getInputStream() throws IOException {
    return CompressionCodec.wrapInputStream(getName(), getRawInputStream());
  }
}
