package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.data.ByteArray;
import ciir.jfoley.chai.coders.data.DataChunk;
import ciir.jfoley.chai.io.StreamFns;

import javax.annotation.Nonnull;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jfoley
 */
public class ASCII {
  public static class FixedLength extends Coder<String> {
    public final int length;

    public FixedLength(int length) {
      this.length = length;
    }

    @Nonnull
    @Override
    public Class<?> getTargetClass() {
      return String.class;
    }

    @Override
    public boolean knowsOwnSize() {
      return true;
    }

    @Nonnull
    @Override
    public DataChunk writeImpl(String obj) throws IOException {
      byte[] output = new byte[length];
      for (int i = 0; i < obj.length(); i++) {
        char c = obj.charAt(i);
        assert(c < 255 && c > 0);
        output[i] = (byte) (0xff & c);
      }
      return new ByteArray(output);
    }

    @Nonnull
    @Override
    public String readImpl(InputStream inputStream) throws IOException {
      byte[] data = StreamFns.readBytes(inputStream, length);

      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < length; i++) {
        if(data[i] == 0) break;
        sb.append((char) data[i]);
      }

      return sb.toString();
    }
  }

  public static class NullTerminated extends Coder<String> {

    @Nonnull
    @Override
    public Class<?> getTargetClass() {
      return String.class;
    }

    @Override
    public boolean knowsOwnSize() {
      return true;
    }

    @Nonnull
    @Override
    public DataChunk writeImpl(String obj) throws IOException {
      byte[] data = new byte[obj.length()+1];
      for (int i = 0; i < obj.length(); i++) {
        char c = obj.charAt(i);
        assert(c < 255 && c > 0);
        data[i] = (byte) (c & 0xff);
      }
      data[obj.length()] = 0;
      return new ByteArray(data);
    }

    @Nonnull
    @Override
    public String readImpl(InputStream inputStream) throws IOException {
      StringBuilder sb = new StringBuilder();
      while(true) {
        int ch = inputStream.read();
        if(ch == -1) throw new EOFException();
        if(ch == 0) break;
        assert(ch > 0 && ch < 255);
        sb.append((char) (0xff & ch));
      }
      return sb.toString();
    }
  }
}
