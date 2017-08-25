package ciir.jfoley.chai.io;

import ciir.jfoley.chai.Encodings;
import ciir.jfoley.chai.errors.FatalError;
import ciir.jfoley.chai.lang.Module;
import org.apache.commons.compress.utils.IOUtils;

import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;

/**
 * @author jfoley.
 */
public class IO extends Module {

  /** Used for transfer operations */
  public static int BUFFER_SIZE = 8192;

  /** This class exists only to provide static methods. */
  private IO() {}

  /**
   * Close anything!
   * TODO: close with reflection if available?
   */
  public static void close(Object obj) {
    try {
      if(obj == null) return;
      if (obj instanceof Closeable) {
        ((Closeable) obj).close();
      } else if (obj instanceof AutoCloseable) {
        ((AutoCloseable) obj).close();
      } else if(obj instanceof XMLStreamWriter) {
        ((XMLStreamWriter) obj).close();
      }
    } catch (Exception ex) {
      throw new FatalError(ex);
    }
  }

  /**
   * Read all data or "slurp" a Reader.
   * @param reader the Reader to read from; doesn't have to be buffered.
   * @param bufferSize the amount of bytes to read at once, usually 4k or so.
   * @return the total read character data as a String.
   * @throws IOException
   */
  public static String readAll(Reader reader, int bufferSize) throws IOException {
    final StringBuilder contents = new StringBuilder();
    char buf[] = new char[bufferSize];
    while(true) {
      int amt = reader.read(buf);
      if(amt < 0) break;
      contents.append(buf, 0, amt);
    }
    return contents.toString();
  }

  public static String readAll(Reader reader) throws IOException {
    return readAll(reader, BUFFER_SIZE);
  }

  /** Slurp a file, inspired by Clojure's naming. */
  public static String slurp(File path) throws IOException {
    try(Reader reader = openReader(path.getAbsolutePath())) {
      return readAll(reader);
    }
  }

  /** Spit a string to a file, inspired by Clojure's naming. */
  public static void spit(String data, File output) throws IOException {
    try (PrintWriter pw = openPrintWriter(output.getAbsolutePath())) {
      pw.print(data);
      pw.flush();
    }
  }

  /**
   * Spit to an outputStream. Note that this closes the output stream, so only use this if you're done with the stream!
   * @param data
   * @param output
   * @throws IOException
   */
  public static void spit(String data, OutputStream output) throws IOException {
    PrintWriter pw = new PrintWriter(output);
    pw.print(data);
    pw.flush();
    output.close();
  }

  public static BufferedReader openReader(String file) throws IOException {
    return new BufferedReader(new InputStreamReader(openInputStream(file), Encodings.UTF8));
  }

  public static InputStream openInputStream(String file) throws IOException {
    return CompressionCodec.openInputStream(file);
  }
  public static OutputStream openOutputStream(String file) throws IOException {
    return CompressionCodec.openOutputStream(file);
  }

  public static PrintWriter openPrintWriter(String file) throws IOException {
    return new PrintWriter(openOutputStream(file));
  }
  public static PrintWriter openPrintWriter(File fp) throws IOException {
    return new PrintWriter(openOutputStream(fp));
  }

  public static BufferedReader openReader(File fp) throws IOException {
    return openReader(fp.getAbsolutePath());
  }

  public static BufferedReader stringReader(String s) {
    return new BufferedReader(new StringReader(s));
  }

  public static InputStream openInputStream(File file) throws IOException {
    return openInputStream(file.getAbsolutePath());
  }

  public static OutputStream openOutputStream(File output) throws IOException {
    return openOutputStream(output.getAbsolutePath());
  }

  @Nullable
  public static InputStream resourceStream(String name) throws IOException {
    InputStream resourceAsStream = IO.class.getResourceAsStream(name);
    if(resourceAsStream == null) {
      return null;
    }
    return CompressionCodec.wrapInputStream(name, resourceAsStream);
  }

  @Nullable
  public static BufferedReader resourceReader(String name) throws IOException {
    InputStream stream = resourceStream(name);
    if(stream == null) return null;
    return new BufferedReader(new InputStreamReader(stream, "UTF-8"));
  }

  public static String slurp(InputStream inputStream) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
      return readAll(reader);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static String resource(String name) throws IOException {
    InputStream inputStream = resourceStream(name);
    if(inputStream == null) {
      return null;
    }
    return slurp(inputStream);
  }

  public static byte[] slurpBytes(File tarEntry) throws IOException {
    try (InputStream is = IO.openInputStream(tarEntry)) {
      return IOUtils.toByteArray(is);
    }
  }

  public static void spit(byte[] data, File output) throws IOException {
    try (OutputStream out = IO.openOutputStream(output)) {
      out.write(data);
    }
  }

  public static int countNewlines(File x) throws IOException {
    int count = 0;
    try (InputStream is = IO.openInputStream(x)) {
      byte buf[] = new byte[BUFFER_SIZE];
      while(true) {
        int amt = is.read(buf);
        if(amt < 0) break;
        for (int i = 0; i < amt; i++) {
          if (buf[i] == '\n') { count++; }
        }
      }
      return count;
    }
  }
}
