package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.CoderException;
import ciir.jfoley.chai.coders.data.ByteArray;
import ciir.jfoley.chai.coders.data.DataChunk;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is based on Galago's implementation.
 * @author jfoley
 */
public class VarUInt extends Coder<Integer> {
  public static final VarUInt instance = new VarUInt();

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
  private static int REG_DATA =  0b01111111;

  private static final int max1 = DONE_BIT;
  private static final int max2 = DONE_BIT << 7;
  private static final int max3 = DONE_BIT << 14;
  private static final int max4 = DONE_BIT << 21;

  @Nonnull
  @Override
  public DataChunk writeImpl(@Nonnegative Integer obj) throws IOException {
    int x = obj;

    if(x < max1) {
      return new ByteArray(new byte[] {
          (byte) (x | DONE_BIT)
      });
    } else if(x < max2) {
      return new ByteArray(new byte[] {
          (byte) (x & REG_DATA),
          (byte) ((x >> 7) | DONE_BIT)
      });
    } else if(x < max3) {
      return new ByteArray(new byte[] {
          (byte) (x & REG_DATA),
          (byte) ((x >>> 7) & REG_DATA),
          (byte) ((x >>> 14) | DONE_BIT)
      });
    } else if(x < max4) {
      return new ByteArray(new byte[] {
          (byte) (x & REG_DATA),
          (byte) ((x >>> 7) & REG_DATA),
          (byte) ((x >>> 14) & REG_DATA),
          (byte) ((x >>> 21) | DONE_BIT)
      });
    } else {
      return new ByteArray(new byte[] {
          (byte) (x & REG_DATA),
          (byte) ((x >>> 7) & REG_DATA),
          (byte) ((x >>> 14) & REG_DATA),
          (byte) ((x >>> 21) & REG_DATA),
          (byte) ((x >>> 28) | DONE_BIT)
      });
    }
  }

  public void writePrim(OutputStream out, int x) {
    try {
      if(x < max1) {
        out.write(x | DONE_BIT);
      } else if(x < max2) {
        out.write(x & REG_DATA);
        out.write((x >> 7) | DONE_BIT);
      } else if(x < max3) {
        out.write(x & REG_DATA);
        out.write((x >>> 7) & REG_DATA);
        out.write((x >>> 14) | DONE_BIT);
      } else if(x < max4) {
        out.write(x & REG_DATA);
        out.write((x >>> 7) & REG_DATA);
        out.write((x >>> 14) & REG_DATA);
        out.write((x >>> 21) | DONE_BIT);
      } else {
        out.write(x & REG_DATA);
        out.write((x >>> 7) & REG_DATA);
        out.write((x >>> 14) & REG_DATA);
        out.write((x >>> 21) & REG_DATA);
        out.write((x >>> 28) | DONE_BIT);
      }
    } catch (IOException e) {
      throw new CoderException(e, this.getClass());
    }
  }

  public void write(OutputStream out, @Nonnegative Integer obj) {
    writePrim(out, obj);
  }

  @Nonnegative
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
        result |= ((b & REG_DATA) << (position * 7));
        break;
      } else {
        result |= (b << (position * 7));
      }
    }

    assert(result >= 0);
    return result;
  }
}
