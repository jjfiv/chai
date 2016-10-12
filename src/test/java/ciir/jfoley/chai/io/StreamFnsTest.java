package ciir.jfoley.chai.io;

import ciir.jfoley.chai.random.Sample;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

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
      try (InputStream is = new BufferedInputStream(new GZIPInputStream(StreamFns.fromByteBuffer(ByteBuffer.wrap(gzipped))))) {
        assertTrue(StreamFns.hasMoreData(is));
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

  @Test
  public void testChannel() throws IOException {
    Random rand = new Random();
    byte[] data = new byte[4096];
    rand.nextBytes(data);
    try(TemporaryFile tmpfile = new TemporaryFile("StreamFnsTest")) {
      IO.spit(data, tmpfile.get()); // fill with random data
      FileChannel channel = FileChannel.open(tmpfile.get().toPath(), StandardOpenOption.READ);
      ByteBuffer byteBuffer = StreamFns.readChannel(channel, 4096);
      byte[] read = StreamFns.readAll(byteBuffer);
      assertArrayEquals(data, read);
    }
  }
}