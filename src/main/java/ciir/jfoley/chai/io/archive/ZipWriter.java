package ciir.jfoley.chai.io.archive;

import ciir.jfoley.chai.Encodings;
import ciir.jfoley.chai.fn.SinkFn;
import ciir.jfoley.chai.io.IO;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A wrapper around a ZipOutputStream that allows simplified access to Java's Zip API.
 * @author jfoley.
 */
public class ZipWriter implements Closeable, Flushable {
  private final ZipOutputStream zos;

  public ZipWriter(@Nonnull String fileName) throws IOException {
    this.zos = new ZipOutputStream(IO.openOutputStream(fileName));
  }

  public void writeUTF8(@Nonnull String name, @Nonnull String contents) throws IOException {
    write(name, contents.getBytes(Encodings.UTF8));
  }
  public void write(@Nonnull String name, @Nonnull byte[] data) throws IOException {
    ZipEntry forWrite = new ZipEntry(name);
    forWrite.setSize(data.length);
    zos.putNextEntry(forWrite);
    zos.write(data);
    zos.closeEntry();
  }

  /**
   * This method allows a lambda to write to a temporary buffer as an outputs stream that then calculates length and puts it into the zip file.
   * @param name the name of the new zip entry.
   * @param fn the lambda that will receive an output stream.
   * @throws IOException
   */
  public void write(@Nonnull String name, @Nonnull SinkFn<OutputStream> fn) throws IOException {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      fn.process(baos);
      baos.close();
      write(name, baos.toByteArray());
    }
  }

  @Override
  public void close() throws IOException {
    zos.close();
  }

  @Override
  public void flush() throws IOException {
    zos.flush();
  }
}
