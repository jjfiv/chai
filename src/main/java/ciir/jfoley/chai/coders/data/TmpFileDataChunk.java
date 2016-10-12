package ciir.jfoley.chai.coders.data;

import ciir.jfoley.chai.IntMath;
import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.io.StreamFns;
import ciir.jfoley.chai.io.TemporaryFile;
import ciir.jfoley.chai.lang.annotations.EmergencyUseOnly;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import static java.nio.file.StandardOpenOption.*;

/**
 * This data chunk immediately starts using a temporary file for storage of other DataChunks written to it.
 * Don't use this class directly; it's very slow that way. Use it through SmartDataChunk instead.
 * @see SmartDataChunk
 * @author jfoley
 */
@EmergencyUseOnly
class TmpFileDataChunk implements MutableDataChunk {
  private final TemporaryFile tmp;
  private final FileChannel channel;
  private long size;

  public TmpFileDataChunk() throws IOException {
    this.tmp = new TemporaryFile("tfdc", "datachunk");
    this.channel = FileChannel.open(tmp.get().toPath(), CREATE, WRITE, READ);
    this.size = 0L;
  }
  @Override
  public int getByte(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long byteCount() {
    return size;
  }

  @Override
  public <T> void add(Coder<T> coder, T obj) {
    add(coder.writeData(obj));
  }
  @Override
  public void add(byte[] data) {
    add(ByteBuffer.wrap(data));
  }
  @Override
  public void add(DataChunk chunk) {
    try {
      size += chunk.byteCount();
      chunk.write(channel);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  @Override
  public void add(ByteBuffer buff) {
    try {
      size += buff.limit();
      channel.write(buff);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ByteBuffer asByteBuffer() {
    try {
      return channel.map(FileChannel.MapMode.READ_ONLY, 0, IntMath.fromLong(size)).duplicate();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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
    return Channels.newInputStream(channel);
  }

  @Override
  public void write(OutputStream out) throws IOException {
    write(Channels.newChannel(out));
  }

  @Override
  public void write(WritableByteChannel out) throws IOException {
    channel.transferTo(0, size, out);
  }

  @Override
  public void close() throws IOException {
    channel.close();
    tmp.close();
  }
}
