package ciir.jfoley.chai.xml;

import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.io.TemporaryFile;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author jfoley.
 */
public class XNodeTest {
	@Test
	public void simpleSelect() throws ParserConfigurationException, SAXException, IOException {
		XNode doc = ChaiXML.fromString("<a><b>Hello</b><c>World</c><b>Frank</b></a>");

		List<XNode> bs = doc.selectByTag("b");
		assertEquals(2, bs.size());
		Assert.assertEquals("Hello", bs.get(0).getText());
		Assert.assertEquals("Frank", bs.get(1).getText());

		List<XNode> cs = doc.selectByTag("c");
		assertEquals(1, cs.size());
		Assert.assertEquals("World", cs.get(0).getText());
	}

	@Test
	public void testAttrs() throws ParserConfigurationException, SAXException, IOException {
		XNode doc = ChaiXML.fromString("<a><b key=\"value\" key2=\"value2\">Hello</b></a>");

		List<XNode> bs = doc.selectByTag("b");
		assertEquals(1, bs.size());
		XNode b = bs.get(0);
		Assert.assertEquals("Hello", b.getText());

		// Has single text child.
		Assert.assertEquals(1, b.getChildren().size());
		assertTrue(b.hasChildren());

		Map<String,String> attrs = b.attrs();
		assertEquals(2, attrs.size());
		assertEquals("value", attrs.get("key"));
		assertEquals("value2", attrs.get("key2"));

	}

	@Test
	public void testFromFile() throws IOException, ParserConfigurationException, SAXException {
		try (TemporaryFile tmpf = new TemporaryFile(".xml")) {
			IO.spit("<doc>\n\t<x key=\"value\" />\n</doc>\n", tmpf.get());
			XNode doc = ChaiXML.fromFile(tmpf.getPath());
			List<XNode> xs = doc.selectByTag("x");
			assertEquals(1, xs.size());
			Assert.assertEquals("value", xs.get(0).attr("key"));
		}
	}
}