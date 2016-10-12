package ciir.jfoley.chai.coders.data;

import ciir.jfoley.chai.IntMath;
import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.collections.util.ArrayFns;
import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.jvm.MemoryNotifier;
import ciir.jfoley.chai.lang.annotations.EmergencyUseOnly;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * The SmartDataChunk is either backed by a {@link BufferList} or a {@link TmpFileDataChunk}
 * If the size exceeds a given amount, it will move out of memory and onto disk.
 * Also, if you call flush(), or if the {@link MemoryNotifier} does, it forces the data to disk.
 *
 * Call flush() before you do any reading, or MemoryNotifier might do it for you while you're reading :(
 *
 * @see BufferList
 * @see TmpFileDataChunk
 * @see MemoryNotifier
 * @author jfoley
 */
public class SmartDataChunk implements MutableDataChunk, Flushable {
  private ByteBuilder bufferList;
  private TmpFileDataChunk tmpFile;
  private final long minFlushAmount;
  private final long defaultFlushAmount;

  /**
   * Default, don't flush if less than 16KiB, flush automatically when you reach 16MiB
   */
  public SmartDataChunk() {
    this(16L << 10, 16L << 20);
  }
  public SmartDataChunk(long minFlushAmount, long defaultFlushAmount) {
    this.minFlushAmount = minFlushAmount;
    this.defaultFlushAmount = defaultFlushAmount;
    MemoryNotifier.register(this);
    bufferList = new ByteBuilder();
    tmpFile = null;
  }


  @Override
  public synchronized <T> void add(Coder<T> coder, T obj) {
    bufferList.add(coder, obj);
    checkSize();
  }

  @Override
  public synchronized void add(ByteBuffer data) {
    bufferList.add(data);
    checkSize();
  }

  @Override
  public synchronized void add(byte[] data) {
    bufferList.add(data);
    checkSize();
  }

  @Override
  public synchronized void add(DataChunk data) {
    bufferList.add(data);
    checkSize();
  }

  public synchronized void checkSize() {
    if(bufferList.byteCount() >= defaultFlushAmount) {
      try {
        flush();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public synchronized int getByte(int index) {
    if(tmpFile != null) {
      if(index < tmpFile.byteCount()) {
        return tmpFile.getByte(index);
      } else {
        return bufferList.getByte(IntMath.fromLong(index - tmpFile.byteCount()));
      }
    }
    return bufferList.getByte(index);
  }

  @Override
  public synchronized long byteCount() {
    long tfbc = (tmpFile == null) ? 0 : tmpFile.byteCount();
    return tfbc + bufferList.byteCount();
  }

  @EmergencyUseOnly
  @Override
  public ByteBuffer asByteBuffer() {
    return ByteBuffer.wrap(asByteArray());
  }

  @EmergencyUseOnly
  @Override
  public synchronized byte[] asByteArray() {
    if(tmpFile != null) {
      return ArrayFns.concat(tmpFile.asByteArray(), bufferList.asByteArray());
    } else {
      return bufferList.asByteArray();
    }
  }

  @Override
  public synchronized InputStream asInputStream() {
    if(tmpFile!=null) {
      return new SequenceInputStream(tmpFile.asInputStream(), bufferList.asInputStream());
    } else {
      return bufferList.asInputStream();
    }
  }

  @Override
  public synchronized void write(OutputStream out) throws IOException {
    if(tmpFile != null) {
      tmpFile.write(out);
    }
    bufferList.write(out);
  }

  @Override
  public synchronized void write(WritableByteChannel out) throws IOException {
    if(tmpFile != null) {
      tmpFile.write(out);
    }
    bufferList.write(out);
  }

  @Override
  public void close() throws IOException {
    MemoryNotifier.unregister(this);
    bufferList.close();
    IO.close(tmpFile);
  }

  @Override
  public synchronized void flush() throws IOException {
    // Only flush if above a reasonable point.
    if(bufferList.byteCount() < minFlushAmount) {
      return;
    }

    if (tmpFile == null) {
      tmpFile = new TmpFileDataChunk();
    }
    if (!bufferList.isEmpty()) {
      tmpFile.add(bufferList);
      bufferList.clear();
    }
  }
}
