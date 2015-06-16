package ciir.jfoley.chai.io.inputs;

import ciir.jfoley.chai.fn.ESinkFn;
import ciir.jfoley.chai.io.CompressionCodec;
import ciir.jfoley.chai.io.Directory;
import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.io.TemporaryDirectory;
import ciir.jfoley.chai.io.archive.ZipWriter;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class InputFinderTest {

  @Test
  public void testSimpleCompression() throws Exception {
    InputFinder finder = InputFinder.Default();
    String testData = "Hello World!";

    List<String> found = new ArrayList<>();
    try (TemporaryDirectory tmpdir = new TemporaryDirectory()) {
      IO.spit(testData, tmpdir.child("test.bz2"));
      IO.spit(testData, tmpdir.child("test.bz"));
      IO.spit(testData, tmpdir.child("test.gz"));

      for (InputContainer container : finder.findAllInputs(tmpdir)) {
        for (InputStreamable input : container.getInputs()) {
          found.add(IO.slurp(input.getInputStream()));
        }
      }
    }

    assertEquals(3, found.size());
    for (String s : found) {
      assertEquals(testData, s);
    }
  }

  @Test
  public void testRecursive() throws Exception {
    InputFinder finder = InputFinder.Default();
    String testData = "Hello World!";

    List<String> found = new ArrayList<>();
    try (TemporaryDirectory tmpdir = new TemporaryDirectory()) {
      IO.spit(testData, tmpdir.child("test.bz2"));
      Directory subdir = tmpdir.childDir("a").childDir("b").childDir("c");
      IO.spit(testData, subdir.child("test.bz"));
      IO.spit(testData, subdir.child("test.gz"));

      for (InputContainer container : finder.findAllInputs(tmpdir)) {
        for (InputStreamable input : container.getInputs()) {
          found.add(IO.slurp(input.getInputStream()));
        }
      }
    }

    assertEquals(3, found.size());
    for (String s : found) {
      assertEquals(testData, s);
    }
  }

  @Test
  public void testZip() throws Exception {
    InputFinder finder = InputFinder.Default();
    final String testData = "Hello World!";

    List<String> found = new ArrayList<>();
    try (TemporaryDirectory tmpdir = new TemporaryDirectory()) {
      try (ZipWriter writer = new ZipWriter(tmpdir.childPath("test.zip"))) {
        writer.writeUTF8("test.txt", testData);
        writer.write("test.bz2", new ESinkFn<OutputStream>() {
          @Override
          public void processE(OutputStream input) throws IOException {
            IO.spit(testData, new BZip2CompressorOutputStream(input));
          }
        });
        writer.write("test.gz", new ESinkFn<OutputStream>() {
          @Override
          public void processE(OutputStream input) throws IOException {
            IO.spit(testData, new GZIPOutputStream(input));
          }
        });
      }
      IO.spit(testData, tmpdir.child("test.bz2"));

      for (InputContainer container : finder.findAllInputs(tmpdir)) {
        for (InputStreamable input : container.getInputs()) {
          found.add(IO.slurp(input.getInputStream()));
        }
      }
    }

    assertEquals(4, found.size());
    for (String s : found) {
      assertEquals(testData, s);
    }
  }


  @Test
  public void testTar() throws IOException {
    InputFinder finder = InputFinder.Default();
    final String testData = "Hello World!";

    List<String> found = new ArrayList<>();
    try (TemporaryDirectory tmpdir = new TemporaryDirectory()) {

      List<String> tarEntries = new ArrayList<>();
      tarEntries.add("test.bz2");
      tarEntries.add("test.gz");
      tarEntries.add("test.txt");

      try (TarArchiveOutputStream taos = new TarArchiveOutputStream(IO.openOutputStream(tmpdir.childPath("test.tar.gz")))) {
        for (String name : tarEntries) {
          byte[] data = testData.getBytes("UTF-8");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          try (OutputStream out = CompressionCodec.wrapOutputStream(name, baos)) {
            out.write(data);
          }
          byte[] actualData = baos.toByteArray();

          TarArchiveEntry header = new TarArchiveEntry(name);
          header.setSize(actualData.length);
          taos.putArchiveEntry(header);
          taos.write(actualData);
          taos.closeArchiveEntry();
        }
      }

      for (InputContainer container : finder.findAllInputs(tmpdir)) {
        for (InputStreamable input : container.getInputs()) {
          found.add(IO.slurp(input.getInputStream()));
        }
      }
    }

    assertEquals(3, found.size());
    for (String s : found) {
      assertEquals(testData, s);
    }
  }
}