package ciir.jfoley.chai.io.compress;

import ciir.jfoley.chai.io.CompressionCodec;
import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author jfoley.
 */
public class ZStdCodec implements CompressionCodec.Impl {
  @Override
  public boolean matchesFileName(String fileName) {
    return fileName.endsWith(".zst");
  }

  @Override
  public InputStream openReader(InputStream fp) throws IOException {
    return new ZstdInputStream(fp);
  }

  @Override
  public OutputStream openWriter(OutputStream fp) throws IOException {
    return new ZstdOutputStream(fp);
  }
}
