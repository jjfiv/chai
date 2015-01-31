package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.Encodings;
import ciir.jfoley.chai.collections.util.IterableFns;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RWMapViewTest {
	@Test
	public void testMapView() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1,11);
		map.put(2,22);
		map.put(3,33);

		Map<String, String> stringsView = new RWMapView<>(map, Encodings.IntAsDecimal, Encodings.IntAsDecimal);

		assertEquals("11", stringsView.get("1"));
		assertEquals("22", stringsView.get("2"));
		assertEquals("33", stringsView.get("3"));
		assertNull(stringsView.get("4"));
		assertNull(map.get(4));
		assertEquals(3, stringsView.size());

		assertEquals(Arrays.asList("1", "2", "3"),
			IterableFns.sorted(stringsView.keySet()));
		assertEquals(Arrays.asList("11", "22", "33"),
			stringsView.values());

		// modify
		stringsView.put("4", "44");
		map.put(5,55);

		// re-test
		assertEquals("44", stringsView.get("4"));
		assertEquals("55", stringsView.get("5"));
		assertEquals(5, stringsView.size());

		assertEquals(Arrays.asList("1", "2", "3", "4", "5"),
			IterableFns.sorted(stringsView.keySet()));
		assertEquals(Arrays.asList("11", "22", "33", "44", "55"),
			stringsView.values());
	}

}