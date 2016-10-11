package ciir.jfoley.chai.io.archive;

import ciir.jfoley.chai.io.TemporaryFile;
import ciir.jfoley.chai.random.Sample;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley.
 */
public class ZipArchiveEntryTest {

  @Test
  public void testBasic() throws Exception {
    Random rand = new Random();
    // build random map
    Map<String,String> randomContents = new HashMap<>();
    List<String> rands = Sample.strings(rand, 100);
    for (int i = 0; i < rands.size(); i++) {
      randomContents.put(Integer.toString(i), rands.get(i));
    }

    try (TemporaryFile tf = new TemporaryFile("test", ".zip")) {
      // write data
      try (ZipWriter zw = new ZipWriter(tf.getPath())) {
        for (Map.Entry<String, String> kv : randomContents.entrySet()) {
          zw.writeUTF8(kv.getKey(), kv.getValue());
        }
      }
      // read data
      try (ZipArchive zr = ZipArchive.open(tf.getPath())) {
        for (ZipArchiveEntry ze : zr.listEntries()) {
          String key = ze.getName();
          String value = ze.slurp();
          assertEquals(value, randomContents.get(key));
        }
      }

    }
  }
}