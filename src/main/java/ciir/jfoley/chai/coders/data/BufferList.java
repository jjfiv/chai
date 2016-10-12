package ciir.jfoley.chai.coders.data;

import ciir.jfoley.chai.IntMath;
import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.io.StreamFns;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * During an indexing operation, it turns out this is really inefficient because it hangs onto garbage, pushes things into deeper heaps, etc. Replacing with ByteBuilder in most places.
 *
 * It is helpful when you want to temporarily assemble a length-prefix of a large amount of data that might be stored in a temporary file.
 *
 * @see ByteBuilder
 * @author jfoley
 */
public class BufferList implements MutableDataChunk {
  List<DataChunk> bufs;

  public BufferList() {
    this.bufs = new ArrayList<>();
  }

  public BufferList(Collection<? extends DataChunk> data) {
    this.bufs = new ArrayList<>(data);
  }

  /** Returns the total size in bytes. */
  @Override
  public long byteCount() {
    int size = 0;
    for (DataChunk buf : bufs) {
      size += buf.byteCount();
    }
    return size;
  }

  @Override
  public <T> void add(Coder<T> coder, T obj) {
    bufs.add(coder.writeData(obj));
  }
  @Override
  public void add(ByteBuffer data) {
    bufs.add(ByteBufferDataChunk.of(data));
  }
  @Override
  public void add(byte[] data) {
    add(ByteBuffer.wrap(data));
  }

  @Override
  public void add(DataChunk data) {
    if(data instanceof BufferList) {
      bufs.addAll(((BufferList) data).bufs);
    } else {
      bufs.add(data);
    }
  }

  @Override
  public ByteBuffer asByteBuffer() {
    ByteBuffer output = ByteBuffer.allocate((int) byteCount());
    for (DataChunk buf : bufs) {
      output.put(buf.asByteBuffer());
    }
    // Because put moves the position() of the buffer, we need to rewind before we return it.
    output.rewind();
    return output;
  }

  @Override
  public byte[] asByteArray() {
    try {
      return StreamFns.readAll(asByteBuffer());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public InputStream asInputStream() {
    // Kind of a hack; might be more performant to be lazy about it.
    return new ByteArrayInputStream(asByteBuffer().array());
  }

  @Override
  public void write(OutputStream out) throws IOException {
    for (DataChunk buf : bufs) {
      buf.write(out);
    }
  }

  @Override
  public void write(WritableByteChannel out) throws IOException {
    for (DataChunk buf : bufs) {
      buf.write(out);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < IntMath.fromLong(byteCount()); i++) {
      sb.append(String.format("%02x", 0xff & getByte(i))).append(' ');
    }
    return sb.toString();
  }

  @Override
  public int getByte(int index) {
    int start = 0;
    for (DataChunk buf : bufs) {
      if (index < start + buf.byteCount()) {
        return buf.getByte(index - start);
      }
      start += buf.byteCount();
    }
    return -1;
  }

  @Override
  public void close() throws IOException {
    for (DataChunk buf : bufs) {
      buf.close();
    }
  }

  public void clear() {
    IO.close(this);
    bufs.clear();
  }

  public boolean isEmpty() {
    return bufs.isEmpty();
  }

  public DataChunk compact() {
    return ByteBufferDataChunk.of(asByteBuffer());
  }
}
