package ciir.jfoley.chai.io.compress;

/**
 * @author jfoley.
 */
public class BZipCodec extends ReflectionCodec {
	@Override
	public boolean matchesFileName(String fileName) {
		return fileName.endsWith(".bz2") || fileName.endsWith(".bz");
	}

	@Override
	public String getInputClassName() {
		return "org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream";
	}

	@Override
	public String getOutputClassName() {
		return "org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream";
	}

}
