package ciir.jfoley.chai.io;

import ciir.jfoley.chai.Encodings;
import ciir.jfoley.chai.errors.FatalError;
import ciir.jfoley.chai.lang.Module;

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
      if(amt < buf.length) break;
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
    }
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
}
