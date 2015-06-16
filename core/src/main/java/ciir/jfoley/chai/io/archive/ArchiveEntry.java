package ciir.jfoley.chai.io.archive;

import ciir.jfoley.chai.io.LinesIterable;
import ciir.jfoley.chai.io.inputs.InputStreamable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * A Generic entry in a ZIP or TAR kind of archive.
 * Keeps a reference to the source file so that it can provide input interfaces as member functions.
 * {:link ZipArchiveEntry}.
 */
public interface ArchiveEntry extends InputStreamable {
  boolean isDirectory();

  /**
   * Return an input stream reading the data of this archive entry.
   * @return a InputStream containing the data of this entry (after inherent decompression).
   * @throws IOException
   */
  InputStream getInputStream() throws IOException;
  /**
   * Read the data as a Reader.
   * @return a BufferedReader reading the UTF-8 data of this archive entry
   * @throws IOException
   */
  BufferedReader getReader() throws IOException;
  /**
   * Read the data a line at a time.
   * @return a LinesIterable reading the UTF-8 data of this archive entry.
   * @throws IOException for encoding or disk errors.
   */
  LinesIterable getLines() throws IOException;

  /**
   * The name of the file inside the archive.
   * @return The name of the archive entry.
   */
  String getName();
}
