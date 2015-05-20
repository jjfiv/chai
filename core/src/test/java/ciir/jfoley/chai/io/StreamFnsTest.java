package ciir.jfoley.chai.io;

import ciir.jfoley.chai.random.Sample;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author jfoley
 */
public class StreamFnsTest {

  @Test
  public void testReadBytesGZIP() throws Exception {
    for (String str : Sample.strings(new Random(), 100)) {
      byte[] data = str.getBytes("UTF-8");

      byte[] gzipped;
      try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
           GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
        gzos.write(data);
        gzos.close();
        gzipped = baos.toByteArray();
      }

      byte[] after_data;
      try (InputStream is = new GZIPInputStream(StreamFns.fromByteBuffer(ByteBuffer.wrap(gzipped)))) {
        after_data = StreamFns.readBytes(is, data.length);
      }

      assertArrayEquals(data, after_data);
    }
  }

  @Test
  public void testReadBytes() throws Exception {
    for (String str : Sample.strings(new Random(), 100)) {
      byte[] data = str.getBytes("UTF-8");
      byte[] after_data = StreamFns.readAll(ByteBuffer.wrap(data));
      assertArrayEquals(data, after_data);
    }
  }
}