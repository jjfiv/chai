package ciir.jfoley.chai.io.compress;

import ciir.jfoley.chai.io.CompressionCodec;
import com.ning.compress.lzf.LZFInputStream;
import com.ning.compress.lzf.LZFOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author jfoley
 */
public class LZFCodec implements CompressionCodec.Impl {
  @Override
  public boolean matchesFileName(String fileName) {
    return fileName.endsWith(".lzf");
  }

  @Override
  public InputStream openReader(InputStream fp) throws IOException {
    return new LZFInputStream(fp);
  }

  @Override
  public OutputStream openWriter(OutputStream fp) throws IOException {
    return new LZFOutputStream(fp);
  }
}
