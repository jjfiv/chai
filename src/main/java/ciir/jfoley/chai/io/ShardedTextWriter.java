package ciir.jfoley.chai.io;

import ciir.jfoley.chai.fn.SinkFn;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author jfoley
 */
public class ShardedTextWriter implements Closeable, SinkFn<String> {
  public final Directory outputDir;
  public final String baseName;
  public final String extension;
  private int limit;
  private int index;
  private int nextIdentifier = 0;
  private PrintWriter writer;

  public ShardedTextWriter(Directory outputDir, String baseName, String extension, int limit) throws IOException {
    this.outputDir = outputDir;
    this.baseName = baseName;
    this.extension = extension;
    this.limit = limit;
    this.index = 0;
    this.writer = null;
    shiftToNextWriter();
  }

  public String getName(int index) {
    return baseName+"."+index+"."+extension;
  }

  void shiftToNextWriter() throws IOException {
    System.err.println("ShardedTextWriter::shiftToNextWriter");
    int current = nextIdentifier++;
    if(writer != null) {
      writer.close();
    }
    writer = IO.openPrintWriter(outputDir.childPath(getName(current)));
    index = 0;
  }

  @Override
  public void close() throws IOException {
    System.err.println("ShardedTextWriter::close "+getName(nextIdentifier));
    writer.close();
  }

  @Override
  public void process(String input) {
    if(index >= limit) {
      try {
        shiftToNextWriter();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    writer.print(input);
    index++;
  }

  public void print(String dat) {
    process(dat);
  }
  public void println(String dat) {
    process(dat+"\n");
  }
}