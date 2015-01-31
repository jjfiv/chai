package edu.umass.cs.ciir.jfoley.chai.collections;

import edu.umass.cs.ciir.jfoley.chai.fn.TransformFn;
import edu.umass.cs.ciir.jfoley.chai.stream.ChaiStream;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FnIterableTest {
	@Test
	public void testMap() {
		List<String> data = ChaiStream.create(Arrays.asList(1, 2, 3)).map(new TransformFn<Integer, Integer>() {
			@Override
			public Integer transform(Integer input) {
				return input * 2;
			}
		}).map(new TransformFn<Integer, String>() {

			@Override
			public String transform(Integer input) {
				return Integer.toString(input);
			}
		}).intoList();

		assertEquals(Arrays.asList("2","4", "6"), data);
	}

}