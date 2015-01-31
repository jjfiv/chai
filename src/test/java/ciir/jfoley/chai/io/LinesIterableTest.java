package ciir.jfoley.chai.io;

import ciir.jfoley.chai.collections.IterableFns;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class LinesIterableTest {
	@Test
	public void testLinesIterable() throws IOException {
		String data = "a\nb\nc";

		assertEquals(Arrays.asList("a", "b", "c"),
			IterableFns.intoList(LinesIterable.of(data)));
	}

}