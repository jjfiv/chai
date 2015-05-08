package ciir.jfoley.chai.xml;

import ciir.jfoley.chai.xml.list.AXNodes;
import ciir.jfoley.chai.xml.list.ListXNodes;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author jfoley.
 */
public class XDoc extends AXNodes {
	Document xmlDoc;
	XDoc(Document xmlDoc) {
		this.xmlDoc = xmlDoc;
	}

	@Override
	public XNodes selectByTag(String tagName) {
		return new ListXNodes(XQuery.findChildrenByTagName(xmlDoc, tagName));
	}

	static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

  public static XDoc fromFile(File fp) throws ParserConfigurationException, IOException, SAXException {
		return fromStream(new FileInputStream(fp));
	}

	public static XDoc fromFile(String path) throws IOException, SAXException, ParserConfigurationException {
		return fromFile(new File(path));
	}

	public static XDoc fromString(String inputXML) throws IOException, SAXException, ParserConfigurationException {
		return fromStream(new ByteArrayInputStream(inputXML.getBytes(StandardCharsets.UTF_8)));
	}

	public static XDoc fromStream(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
		return new XDoc(factory.newDocumentBuilder().parse(inputStream));
	}

	@Override
	public XNode get(int i) {
		return new XNode(xmlDoc.getChildNodes().item(i));
	}

	@Override
	public int size() {
		return xmlDoc.getChildNodes().getLength();
	}
}
