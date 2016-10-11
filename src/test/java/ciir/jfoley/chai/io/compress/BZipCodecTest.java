package ciir.jfoley.chai.io.compress;

import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.io.TemporaryFile;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class BZipCodecTest {

	@Test
	public void bzipWorksIfOnClasspath() throws IOException {
		try(TemporaryFile tmpFile = TemporaryFile.create("abcd", ".bz2")) {
			String path = tmpFile.getPath();
			assertTrue(path.endsWith(".bz2"));
			IO.spit("Hello World!", tmpFile.get());
			assertEquals("Hello World!", IO.slurp(tmpFile.get()));
		}
	}

}