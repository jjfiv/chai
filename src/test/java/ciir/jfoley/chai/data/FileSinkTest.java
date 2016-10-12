package ciir.jfoley.chai.data;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.files.FileSink;
import ciir.jfoley.chai.coders.kinds.CharsetCoders;
import ciir.jfoley.chai.coders.kinds.ListCoder;
import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.io.TemporaryFile;
import ciir.jfoley.chai.random.Sample;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class FileSinkTest {

  @Test
  public void testWrite() throws Exception {
    Coder<String> coder = CharsetCoders.utf8;
    try (TemporaryFile tmpFile = new TemporaryFile("filesink", "tmp")) {
      try(FileSink sink = new FileSink(tmpFile.getPath())) {
        sink.write(coder, "Hello World!");
      }

      InputStream input = IO.openInputStream(tmpFile.getPath());
      assertEquals("Hello World!", coder.read(input));
    }
  }

  @Test
  public void testWriteList() throws Exception {
    ListCoder<String> coder = new ListCoder<>(CharsetCoders.utf8);
    List<String> data = Sample.strings(new Random(), 100);
    try (TemporaryFile tmpFile = new TemporaryFile("filesink", "tmp")) {
      try(FileSink sink = new FileSink(tmpFile.getPath())) {
        sink.write(coder, data);
      }

      InputStream input = IO.openInputStream(tmpFile.getPath());
      assertEquals(data, coder.read(input));
    }
  }

  @Test
  public void testGZipList() throws IOException {
    ListCoder<String> coder = new ListCoder<>(CharsetCoders.utf8);
    List<String> data = Sample.strings(new Random(), 10000);
    assertEquals(10000, data.size());
    try (TemporaryFile tmpFile = new TemporaryFile("filesink", ".tmp.gz")) {
      try(OutputStream sink = IO.openOutputStream(tmpFile.getPath())) {
        coder.write(sink, data);
      }

      InputStream input = IO.openInputStream(tmpFile.getPath());
      assertEquals(data, coder.read(input));
    }
  }

}