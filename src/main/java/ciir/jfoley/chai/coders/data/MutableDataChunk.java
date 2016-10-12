package ciir.jfoley.chai.coders.data;

import ciir.jfoley.chai.coders.Coder;

import java.nio.ByteBuffer;

/**
 * @author jfoley
 */
public interface MutableDataChunk extends DataChunk {
  <T> void add(Coder<T> coder, T obj);
  void add(ByteBuffer data);
  void add(byte[] data);
  void add(DataChunk data);
}
