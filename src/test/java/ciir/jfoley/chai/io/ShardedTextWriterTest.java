package ciir.jfoley.chai.io;

import ciir.jfoley.chai.string.StrUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class ShardedTextWriterTest {
  @Test
  public void testSimple() throws IOException {
    try (TemporaryDirectory tmpdir = new TemporaryDirectory()) {
      try (ShardedTextWriter writer = new ShardedTextWriter(tmpdir, "foo", "txt", 10)) {
        for (int i = 0; i < 100; i++) {
          writer.println(Integer.toString(i));
        }
      }

      for (File file : tmpdir.children()) {
        String name = file.getName();
        String num = StrUtil.takeBefore(StrUtil.takeAfter(name, "foo."), ".txt");
        int idx = Integer.parseInt(num);
        //System.out.println(name +"\t"+IO.slurp(file));
        List<String> lines = LinesIterable.fromFile(file).slurp();
        for (int i = 0; i < lines.size(); i++) {
          String line = lines.get(i);
          int rhs = Integer.parseInt(line);
          assertEquals(idx*10 + i, rhs);
        }
      }
    }
  }

}