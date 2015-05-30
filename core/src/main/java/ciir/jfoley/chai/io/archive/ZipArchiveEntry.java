package ciir.jfoley.chai.io.archive;

import ciir.jfoley.chai.Encodings;
import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.io.LinesIterable;
import ciir.jfoley.chai.lang.annotations.EmergencyUseOnly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/** A ZipEntry that knows what file it came from, and has some simple reading interfaces. */
public class ZipArchiveEntry implements ArchiveEntry {
  private final ZipEntry entry;
  private final ZipFile archive;
  public ZipArchiveEntry(ZipEntry entry, ZipFile archive) {
    this.entry = entry;
    this.archive = archive;
  }

  /**
   * In case the abstraction isn't good enough.
   * @return the underlying Zip Entry.
   */
  @EmergencyUseOnly
  public ZipEntry rawZipEntry() {
    return entry;
  }

  /**
   * In case the abstraction isn't good enough.
   * @return the underlying Zip File.
   */
  @EmergencyUseOnly
  public ZipFile rawZipFile() {
    return archive;
  }

  @Override
  public boolean isDirectory() {
    return this.entry.isDirectory();
  }
  @Override
  public InputStream getInputStream() throws IOException {
    return archive.getInputStream(entry);
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(getInputStream(), Encodings.UTF8));
  }

  /**
   * Return a LinesIterable of this zip entry.
   * @return a {:link Closeable} {:link LinesIterable}.
   */
  @Override
  public LinesIterable getLines() throws IOException {
    return LinesIterable.of(getReader());
  }

  /**
   * Return the name of the file inside this zip.
   * @return name.
   */
  @Override
  public String getName(){
    return entry.getName();
  }

  /**
   * Read contents as UTF8 String all at once... hope it fits in memory! :)
   * @return the string of the contents for this entry.
   * @throws IOException
   */
  public String slurp() throws IOException {
    try (BufferedReader rdr = getReader()) {
      return IO.readAll(rdr);
    }
  }
}
