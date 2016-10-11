package ciir.jfoley.chai.io.archive;

import ciir.jfoley.chai.collections.util.ListFns;
import ciir.jfoley.chai.lang.annotations.EmergencyUseOnly;

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
  private final File source;

  ZipArchive(File source, ZipFile zipFile) {
    this.source = source;
    this.zipFile = zipFile;
  }

  public static ZipArchive open(String path) throws IOException {
    return open(new File(path));
  }
  public static ZipArchive open(File fp) throws IOException {
    return new ZipArchive(fp, new ZipFile(fp));
  }

  /**
   * In case you need rawsort access for some reason.
   * @return the implementation ZipFile.
   */
  @EmergencyUseOnly
  public ZipFile getZipFile() {
    return zipFile;
  }

  @Override
  public List<ZipArchiveEntry> listEntries() {
    List<ZipArchiveEntry> results = new ArrayList<>();
    for (ZipEntry zipEntry : ListFns.collect(zipFile.entries())) {
      results.add(new ZipArchiveEntry(zipEntry, zipFile));
    }
    return results;
  }

  public ZipArchiveEntry getByName(String name) {
    ZipEntry entry = zipFile.getEntry(name);
    if(entry == null) return null;
    return new ZipArchiveEntry(entry, zipFile);
  }

  @Override
  public void close() throws IOException {
    zipFile.close();
  }

  @Override
  public String getName() {
    return source.getName();
  }

  @Override
  public boolean isParallel() {
    return true;
  }

  @Override
  public long estimateCount() {
    return zipFile.size();
  }
}
