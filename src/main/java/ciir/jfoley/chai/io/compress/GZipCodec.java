package ciir.jfoley.chai.io.compress;

import ciir.jfoley.chai.io.CompressionCodec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
* @author jfoley.
*/
public class GZipCodec implements CompressionCodec.Impl {
	@Override
	public boolean matchesFileName(String fileName) {
		return fileName.endsWith(".gz");
	}

	@Override
	public InputStream openReader(InputStream fp) throws IOException {
		return new GZIPInputStream(fp);
	}

	@Override
	public OutputStream openWriter(OutputStream fp) throws IOException {
		return new GZIPOutputStream(fp);
	}
}
