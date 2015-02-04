package ciir.jfoley.chai;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class SpawnTest {

	@Test
	public void testSpawnProcess() throws IOException, InterruptedException {
		try {
			Spawn.doProcess("/usr/bin/which");
			fail("Expected exception -- which needs arguments.");
		} catch (RuntimeException ex) {
			assertNotNull(ex);
		}
		Spawn.doProcess("/usr/bin/which", "ls");
	}

}