package ciir.jfoley.chai.xml;

import ciir.jfoley.chai.lang.LazyPtr;
import ciir.jfoley.chai.lang.Module;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author jfoley.
 */
public class ChaiXML extends Module {
	static LazyPtr<DocumentBuilderFactory> factory = new LazyPtr<>(DocumentBuilderFactory::newInstance);

  public static XNode fromFile(File fp) throws ParserConfigurationException, IOException, SAXException {
		return fromStream(new FileInputStream(fp));
	}

	public static XNode fromFile(String path) throws IOException, SAXException, ParserConfigurationException {
		return fromFile(new File(path));
	}

	public static XNode fromString(String inputXML) throws IOException, SAXException, ParserConfigurationException {
		return fromStream(new ByteArrayInputStream(inputXML.getBytes(StandardCharsets.UTF_8)));
	}

	public static XNode fromStream(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
		return new XNode(factory.get().newDocumentBuilder().parse(inputStream));
	}

}
