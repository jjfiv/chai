package ciir.jfoley.chai.coders.data;

import ciir.jfoley.chai.io.StreamFns;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

/**
 * @author jfoley
 */
public class ByteBufferDataChunk implements DataChunk {
  private final ByteBuffer buffer;
  private ByteBufferDataChunk(ByteBuffer buf) {
    this.buffer = buf;
  }

  @Override
  public int getByte(int index) {
    return buffer.get(index) & 0xff;
  }

  @Override
  public long byteCount() {
    return buffer.limit();
  }

  @Override
  public ByteBuffer asByteBuffer() {
    return buffer.duplicate();
  }

  @Override
  public byte[] asByteArray() {
    if(buffer.hasArray()) return buffer.array();
    try {
      return StreamFns.readAll(buffer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public InputStream asInputStream() {
    return StreamFns.fromByteBuffer(buffer);
  }

  @Override
  public void write(OutputStream out) throws IOException {
    //out.write(buffer.array());
    write(Channels.newChannel(out));
  }

  @Override
  public void write(WritableByteChannel out) throws IOException {
    out.write(buffer);
  }

  public static ByteBufferDataChunk of(ByteBuffer buf) {
    buf.rewind();
    return new ByteBufferDataChunk(buf);
  }
  public static ByteBufferDataChunk of(byte[] data) {
    return new ByteBufferDataChunk(ByteBuffer.wrap(data));
  }

  @Override
  public void close() throws IOException {
    // Nothing.
  }
}
