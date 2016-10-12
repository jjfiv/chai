package ciir.jfoley.chai.coders.files;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.data.DataChunk;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author jfoley
 */
public interface DataSink {
  void write(ByteBuffer buf) throws IOException;

  void write(DataChunk data) throws IOException;

  <T> void write(Coder<T> coder, T obj) throws IOException;

  OutputStream getOutputStream();

  <T> void write(long offset, Coder<T> coder, T value) throws IOException;

  long tell() throws IOException;

  // Go back to a specific offset in this DataSink:
  void seekAbsolute(long offset) throws IOException;
}
