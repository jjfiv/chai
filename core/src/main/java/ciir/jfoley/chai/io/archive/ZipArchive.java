package ciir.jfoley.chai.io.archive;

import ciir.jfoley.chai.collections.util.ListFns;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Wraps a Java standard ZipFile to provide a simpler API.
 */
public class ZipArchive extends Archive<ZipArchiveEntry> {
  private final ZipFile zipFile;

  ZipArchive(ZipFile zipFile) {
    this.zipFile = zipFile;
  }

  public static ZipArchive open(String path) throws IOException {
    return open(new File(path));
  }
  public static ZipArchive open(File fp) throws IOException {
    return new ZipArchive(new ZipFile(fp));
  }

  @Override
  public List<ZipArchiveEntry> listEntries() {
    List<ZipArchiveEntry> results = new ArrayList<>();
    for (ZipEntry zipEntry : ListFns.collect(zipFile.entries())) {
      results.add(new ZipArchiveEntry(zipEntry, zipFile));
    }
    return results;
  }

  @Override
  public void close() throws IOException {
    zipFile.close();
  }
}
