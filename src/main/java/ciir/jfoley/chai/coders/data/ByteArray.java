package ciir.jfoley.chai.coders.data;

import ciir.jfoley.chai.collections.util.ArrayFns;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

/**
 * A useful wrapper around Java's primitive byte[] that implements DataChunk and Comparable.
 * @author jfoley
 */
public class ByteArray implements DataChunk, Comparable<ByteArray> {
  public final byte[] data;
  public ByteArray(byte[] data) {
    this.data = data;
  }

  @Override
  public int getByte(int index) {
    return data[index];
  }

  @Override
  public long byteCount() {
    return data.length;
  }

  @Override
  public ByteBuffer asByteBuffer() {
    return ByteBuffer.wrap(data).duplicate();
  }

  @Override
  public byte[] asByteArray() {
    return data;
  }

  @Override
  public InputStream asInputStream() {
    return new ByteArrayInputStream(data);
  }

  @Override
  public void write(OutputStream out) throws IOException {
    out.write(data);
  }

  @Override
  public void write(WritableByteChannel out) throws IOException {
    out.write(asByteBuffer());
  }

  @Override
  public void close() throws IOException {
    // Nothing.
  }

  @Override
  public int compareTo(@Nonnull ByteArray o) {
    return ArrayFns.compare(this.data, o.data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.data.length; i++) {
      sb.append(String.format("%02x", 0xff & getByte(i))).append(' ');
    }
    return sb.toString();
  }

  @Override
  public boolean equals(Object other) {
    if(other == null) return false;
    if(other instanceof ByteArray) {
      return Arrays.equals(this.data, ((ByteArray) other).data);
    } else if(other instanceof byte[]) {
      return Arrays.equals(this.data, (byte[]) other);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.data);
  }


  public static ByteArray of(DataChunk c) {
    if(c instanceof ByteArray) { return (ByteArray) c; }
    return new ByteArray(c.asByteArray());
  }
}
