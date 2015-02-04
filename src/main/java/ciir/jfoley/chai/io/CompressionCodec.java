package ciir.jfoley.chai.io;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author jfoley.
 */
public class CompressionCodec {
	static ConcurrentHashMap<String, Impl> streamCodecs = new ConcurrentHashMap<>();
	static {
		streamCodecs.put(".gz", new GZipCodec());
	}
	public static interface Impl {
		public boolean matchesFileName(String fileName);
		public InputStream openReader(InputStream fp) throws IOException;
		public OutputStream openWriter(OutputStream fp) throws IOException;
	}

	public static class GZipCodec implements Impl {
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

	public static InputStream openInputStream(String file) throws IOException {
		for (CompressionCodec.Impl codec : streamCodecs.values()) {
			if(codec.matchesFileName(file)) {
				return codec.openReader(new FileInputStream(file));
			}
		}
		return new FileInputStream(file);
	}
	public static OutputStream openOutputStream(String file) throws IOException {
		for (CompressionCodec.Impl codec : streamCodecs.values()) {
			if(codec.matchesFileName(file)) {
				return codec.openWriter(new FileOutputStream(file));
			}
		}
		return new FileOutputStream(file);
	}
}
