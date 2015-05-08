package ciir.jfoley.chai.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * Create and delete temporary files as resources.
 * @author jfoley.
 */
public class TemporaryFile implements Closeable {
	private final File file;

	/** When you only care about the suffix that gets generated. */
	public TemporaryFile(String suffix) throws IOException {
		this("chai", suffix);
	}
	/** When you want to have control over the prefix & suffix of the file. */
	public TemporaryFile(String prefix, String suffix) throws IOException {
		this.file = File.createTempFile(prefix, suffix);
	}

	public File get() {
		return file;
	}

	public String getPath() {
		return file.getAbsolutePath();
	}

	@Override
	public void close() throws IOException {
		boolean status = file.delete();
		if(!status) throw new IOException("Temporary file failed to delete.");
	}

	public static TemporaryFile create(String prefix, String suffix) throws IOException {
		return new TemporaryFile(prefix, suffix);
	}
	public static TemporaryFile create() throws IOException {
		return create("chai", ".tmp");
	}
}
