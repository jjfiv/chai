package ciir.jfoley.chai.io.compress;

import ciir.jfoley.chai.io.Closer;
import ciir.jfoley.chai.io.IO;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BZipCodecTest {

	@Test
	public void bzipExistsOnClasspath() throws IOException {
		BZipCodec codec = new BZipCodec();
		assertNotNull(codec.getInputConstructor());
		assertNotNull(codec.getOutputConstructor());
	}
	@Test
	public void bzipWorksIfOnClasspath() throws IOException {
		try(Closer<File> tmpFile = Closer.of(File.createTempFile("abcd", ".bz2"))) {
			String path = tmpFile.get().getAbsolutePath();
			assertTrue(path.endsWith(".bz2"));
			IO.spit("Hello World!", tmpFile.get());
			assertEquals("Hello World!", IO.slurp(tmpFile.get()));
		}
	}

}