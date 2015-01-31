package edu.umass.cs.ciir.jfoley.chai;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EncodingsTest {

	@Test
	public void testEncodeURLUTF8() throws Exception {
		assertEquals("a+b+c", Encodings.encodeURLUTF8("a b c"));
	}


}