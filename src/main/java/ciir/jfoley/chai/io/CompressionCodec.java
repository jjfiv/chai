package ciir.jfoley.chai.io;

import ciir.jfoley.chai.io.compress.BZipCodec;
import ciir.jfoley.chai.io.compress.GZipCodec;
import ciir.jfoley.chai.io.compress.LZFCodec;
import ciir.jfoley.chai.io.compress.ZStdCodec;
import ciir.jfoley.chai.lang.Module;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jfoley.
 */
public final class CompressionCodec extends Module {
	static ConcurrentHashMap<String, Impl> streamCodecs = new ConcurrentHashMap<>();
	static {
		streamCodecs.put("gz", new GZipCodec());
		streamCodecs.put("zst", new ZStdCodec());
		streamCodecs.put("bz|bz2", new BZipCodec());
		streamCodecs.put("lzf", new LZFCodec());
	}

	@Nonnull
	public static OutputStream wrapOutputStream(@Nonnull String name, @Nonnull OutputStream base) throws IOException {
		for (CompressionCodec.Impl codec : streamCodecs.values()) {
			if (codec.matchesFileName(name)) {
				return codec.openWriter(base);
			}
		}
		return base;
	}

	public interface Impl {
		boolean matchesFileName(String fileName);
		InputStream openReader(InputStream fp) throws IOException;
		OutputStream openWriter(OutputStream fp) throws IOException;
	}

	@Nonnull
	public static InputStream wrapInputStream(@Nonnull String name, @Nonnull InputStream base) throws IOException {
		for (CompressionCodec.Impl codec : streamCodecs.values()) {
			if (codec.matchesFileName(name)) {
				return codec.openReader(base);
			}
		}
		return base;
		//return new BufferedInputStream(base);
	}

	@Nonnull
	public static InputStream openInputStream(String file) throws IOException {
		return wrapInputStream(file, Files.newInputStream(new File(file).toPath(), StandardOpenOption.READ));
	}

	@Nonnull
	public static OutputStream openOutputStream(String file) throws IOException {
		return wrapOutputStream(file, Files.newOutputStream(new File(file).toPath(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE, StandardOpenOption.CREATE));
	}
}
