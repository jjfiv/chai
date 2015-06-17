package ciir.jfoley.chai.io.compress;

import ciir.jfoley.chai.io.CompressionCodec;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author jfoley.
 */
public class BZipCodec implements CompressionCodec.Impl {
	@Override
	public boolean matchesFileName(String fileName) {
		return fileName.endsWith(".bz2") || fileName.endsWith(".bz");
	}

	@Override
	public InputStream openReader(InputStream fp) throws IOException {
		return new BZip2CompressorInputStream(fp);
	}

	@Override
	public OutputStream openWriter(OutputStream fp) throws IOException {
		return new BZip2CompressorOutputStream(fp);
	}

}
