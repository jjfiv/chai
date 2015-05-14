package ciir.jfoley.chai.io.archive;

import ciir.jfoley.chai.Encodings;
import ciir.jfoley.chai.io.LinesIterable;

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

  public ZipEntry rawZipEntry() {
    return entry;
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

  @Override
  public LinesIterable getLines() throws IOException {
    return LinesIterable.of(getReader());
  }

  @Override
  public String getName() {
    return entry.getName();
  }
}
