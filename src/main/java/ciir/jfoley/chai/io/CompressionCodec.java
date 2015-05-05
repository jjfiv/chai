package ciir.jfoley.chai.io;

import ciir.jfoley.chai.io.compress.BZipCodec;
import ciir.jfoley.chai.io.compress.GZipCodec;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jfoley.
 */
public class CompressionCodec {
	static ConcurrentHashMap<String, Impl> streamCodecs = new ConcurrentHashMap<>();
	static {
		streamCodecs.put("gz", new GZipCodec());
		streamCodecs.put("bz|bz2", new BZipCodec());
	}
	public interface Impl {
		boolean matchesFileName(String fileName);
		InputStream openReader(InputStream fp) throws IOException;
		OutputStream openWriter(OutputStream fp) throws IOException;
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
