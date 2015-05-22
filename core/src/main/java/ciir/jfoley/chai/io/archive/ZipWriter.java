package ciir.jfoley.chai.io.archive;

import ciir.jfoley.chai.Encodings;
import ciir.jfoley.chai.fn.SinkFn;
import ciir.jfoley.chai.io.IO;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A wrapper around a ZipOutputStream that allows simplified access to Java's Zip API.
 * @author jfoley.
 */
public class ZipWriter implements Closeable {
  private final ZipOutputStream zos;

  public ZipWriter(String fileName) throws IOException {
    this.zos = new ZipOutputStream(IO.openOutputStream(fileName));
  }

  public void writeUTF8(String name, String contents) throws IOException {
    write(name, contents.getBytes(Encodings.UTF8));
  }
  public void write(String name, byte[] data) throws IOException {
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
  public void write(String name, SinkFn<OutputStream> fn) throws IOException {
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
}
