package ciir.jfoley.chai.data;

import ciir.jfoley.chai.coders.files.FileChannelSource;
import ciir.jfoley.chai.io.StreamFns;
import ciir.jfoley.chai.io.TemporaryFile;
import ciir.jfoley.chai.io.streams.SkipInputStream;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class DataSourceSkipInputStreamTest {

  byte[] random(int amount) {
    Random rand = new Random();
    byte[] data = new byte[amount];
    rand.nextBytes(data);
    return data;
  }

  @Test
  public void FileSourceSIST() throws IOException {
    List<byte[]> raw = new ArrayList<>();
    List<byte[]> cpr = new ArrayList<>();
    int rawblocks = 10;
    int compressedBlocks = 20;
    int blockSize = 4096;

    for (int i = 0; i < rawblocks; i++) {
      raw.add(random(blockSize));
    }
    for (int i = 0; i < compressedBlocks; i++) {
      cpr.add(random(blockSize));
    }

    try (TemporaryFile tmp = new TemporaryFile(".bin")) {
      FileOutputStream os = new FileOutputStream(tmp.get());

      for (byte[] bytes : raw) {
        os.write(bytes);
      }

      try (GZIPOutputStream gzos = new GZIPOutputStream(os)) {
        for (byte[] bytes : cpr) {
          gzos.write(bytes);
        }
      } // finish writing


      try (FileChannelSource source = new FileChannelSource(tmp.getPath())) {

        SkipInputStream skis = source.stream();
        for (byte[] expected : raw) {
          byte[] actual = StreamFns.readBytes(skis, blockSize);
          assertArrayEquals(expected, actual);
        }

        assertEquals(skis.tell(), rawblocks * blockSize);

        // Try continuing the stream as GZIP:
        try (GZIPInputStream is = new GZIPInputStream(skis)) {
          for (byte[] expected : cpr) {
            byte[] block = StreamFns.readBytes(is, blockSize);
            assertArrayEquals(expected, block);
          }
        }

        // Try seeking there directly:
        try (GZIPInputStream is = new GZIPInputStream(source.stream(rawblocks * blockSize))) {
          for (byte[] expected : cpr) {
            byte[] block = StreamFns.readBytes(is, blockSize);
            assertArrayEquals(expected, block);
          }
        }
      }

    } // close tmp file
  }

}