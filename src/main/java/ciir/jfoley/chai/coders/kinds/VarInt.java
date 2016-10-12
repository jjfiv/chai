package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.data.ByteArray;
import ciir.jfoley.chai.coders.data.DataChunk;

import javax.annotation.Nonnull;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * This is based on Galago's implementation, but tweaked, so it can actually handle negative numbers.
 * @author jfoley
 */
public class VarInt extends Coder<Integer> {
  public static final VarInt instance = new VarInt();

  @Nonnull
  @Override
  public Class<?> getTargetClass() {
    return Integer.class;
  }

  @Override
  public boolean knowsOwnSize() {
    return true;
  }

  // Magic bits for this format:
  private static int DONE_BIT =  0b10000000;
  private static int SIGN_BIT =  0b01000000;
  private static int LAST_DATA = 0b00111111;
  private static int REG_DATA =  0b01111111;

  @Nonnull
  @Override
  public DataChunk writeImpl(Integer obj) throws IOException {
    assert(obj != null);
    int x = obj;

    boolean negative = x < 0;
    if(negative) {
      x *= -1;
    }

    byte[] data = new byte[6];
    int put = 0;
    while((x != 0) || (put == 0)) {
      if(x < SIGN_BIT) { // fits in 6 bits:
        data[put++] = (byte) ((x & LAST_DATA) | (negative ? SIGN_BIT | DONE_BIT : DONE_BIT));
        break;
      } else if(x < DONE_BIT) { // fits in 7, perfectly.
        data[put++] = (byte) (x & REG_DATA);
        data[put++] = (byte) (negative ? SIGN_BIT | DONE_BIT : DONE_BIT); // mostly waste last byte :(
        break;
      } else {
        data[put++] = (byte) (x & REG_DATA);
        x >>>= 7;
      }
    }

    assert((data[put-1] & DONE_BIT) != 0);
    assert(put >= 1 && put <= 6);
    return new ByteArray(Arrays.copyOf(data, put));
  }

  @Nonnull
  @Override
  public Integer readImpl(InputStream inputStream) throws IOException {
    return readPrim(inputStream);
  }

  public int readPrim(InputStream inputStream) throws IOException {
    int result = 0;

    for (int position = 0; true; position++) {
      assert position < 6;
      int x= inputStream.read();
      if (x == -1) throw new EOFException();
      int b = x & 0xff;
      if ((b & DONE_BIT) != 0) {
        result |= ((b & LAST_DATA) << (position * 7));
        if((b & SIGN_BIT) != 0) { result *= -1; }
        break;
      } else {
        result |= (b << (position * 7));
      }
    }

    return result;
  }
}
