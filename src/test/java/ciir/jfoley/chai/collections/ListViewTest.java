package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.Encodings;
import ciir.jfoley.chai.codec.Codec;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ListViewTest {

	public static <A,B> void codecTest(Codec<A,B> codec, List<Pair<A,B>> testPairs) {
		assertNotNull(testPairs);
		assertNotNull(codec);

		// Always check null.
		assertNull(codec.wrap(null));
		assertNull(codec.unwrap(null));

		for (Pair<A, B> testPair : testPairs) {
			assertEquals(testPair.left, codec.unwrap(testPair.right));
			assertEquals(testPair.right, codec.wrap(testPair.left));
		}
	}

	@Test
	public void testStringIntCodec() {
		codecTest(Encodings.IntAsDecimal, Arrays.asList(
			Pair.of(3, "3"),
			Pair.of(14, "14"),
			Pair.of(-12345, "-12345")
		));
	}

	@Test
	public void testUTF8Codec() {
		codecTest(Encodings.UTF8Codec, Arrays.asList(
			Pair.of(ByteBuffer.wrap("3".getBytes(Encodings.UTF8)), "3"),
			Pair.of(ByteBuffer.wrap("abcds".getBytes(Encodings.UTF8)), "abcds")
		));
	}

	@Test
	public void saveRamByOnlyStoringIntegers() {
		List<Integer> underlying = new ArrayList<>(Arrays.asList(1,2,3,4,5));
		ListView<Integer, String> wrapped = new ListView<>(underlying, Encodings.IntAsDecimal);

		assertEquals(
			Arrays.asList("1", "2", "3", "4", "5"),
			wrapped
		);

		// modifications
		wrapped.add("6");
		underlying.add(7);

		assertEquals(Arrays.asList(1,2,3,4,5,6,7), underlying);
	}

}