package ciir.jfoley.chai.io.streams;

import ciir.jfoley.chai.coders.files.FileChannelSource;
import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.io.StreamFns;
import ciir.jfoley.chai.io.TemporaryFile;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class CachedSkipInputStreamTest {

  byte[] random(int amount) {
    Random rand = new Random();
    byte[] data = new byte[amount];
    rand.nextBytes(data);
    return data;
  }

  @Test
  public void CachedSIST() throws IOException {
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

        SkipInputStream skis = new CachedSkipInputStream(source.stream(), 709);
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
        try (GZIPInputStream is = new GZIPInputStream(new CachedSkipInputStream(source.stream(rawblocks * blockSize), 88))) {
          for (byte[] expected : cpr) {
            byte[] block = StreamFns.readBytes(is, blockSize);
            assertArrayEquals(expected, block);
          }
        }
      }

    } // close tmp file
  }

  @Test
  public void withinCacheSkip() throws IOException {
    byte[] data = random(20);
    CachedSkipInputStream csis = new CachedSkipInputStream(new NaiveSkipInputStream(new ByteArrayInputStream(data)));
    csis.seek(10);
    byte[] secondHalf = StreamFns.readBytes(csis, 10);
    assertArrayEquals(secondHalf, Arrays.copyOfRange(data, 10, 20));
  }

  @Test
  public void overCacheSkip() throws IOException {
    byte[] data = random(20);
    CachedSkipInputStream csis = new CachedSkipInputStream(new NaiveSkipInputStream(new ByteArrayInputStream(data)), 11);
    csis.seek(10);
    byte[] secondHalf = StreamFns.readBytes(csis, 10);
    assertArrayEquals(secondHalf, Arrays.copyOfRange(data, 10, 20));
  }

  @Test
  public void onDiskSkipTest() throws IOException {
    byte[] data = random(2000);

    try (TemporaryFile tmp = new TemporaryFile(".bin")) {
      IO.spit(data, tmp.get());
      FileChannelSource source = new FileChannelSource(tmp.getPath());
      SkipInputStream sis = new CachedSkipInputStream(source.stream(), 77);

      sis.seekRelative(500);
      sis.seekRelative(500); // 1000
      sis.seekRelative(50);
      sis.seekRelative(-50);
      sis.seekRelative(150);
      sis.seekRelative(-150);

      byte[] secondHalf = StreamFns.readBytes(sis, 1000);
      assertArrayEquals(secondHalf, Arrays.copyOfRange(data, 1000, 2000));
    }
  }
}