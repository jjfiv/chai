package ciir.jfoley.chai.io.compress;

import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.io.TemporaryFile;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompressCodecTest {

	@Test
	public void testAll() throws IOException {
		for (String extension : Arrays.asList(".gz", ".bz2", ".zst", ".lzf")) {
			String testData = "Hello World!";
			smokeTestEncryption(extension, testData);
		}
	}

	private void smokeTestEncryption(String extension, String testData) throws IOException {
		try(TemporaryFile tmpFile = TemporaryFile.create(CompressCodecTest.class.getSimpleName(), extension)) {
			String path = tmpFile.getPath();
			assertTrue(path.endsWith(extension));
			IO.spit(testData, tmpFile.get());
			assertEquals(testData, IO.slurp(tmpFile.get()));
		}
	}

}