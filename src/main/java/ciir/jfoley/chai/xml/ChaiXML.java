package ciir.jfoley.chai.xml;

import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.lang.Module;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author jfoley.
 */
public class ChaiXML extends Module {
	public static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

  public static XNode fromFile(File fp) throws ParserConfigurationException, IOException, SAXException {
		return fromStream(IO.openInputStream(fp));
	}

	public static XNode fromFile(String path) throws IOException, SAXException, ParserConfigurationException {
		return fromFile(new File(path));
	}

	public static XNode fromString(String inputXML) throws IOException, SAXException, ParserConfigurationException {
		return fromStream(new ByteArrayInputStream(inputXML.getBytes(StandardCharsets.UTF_8)));
	}

	public static XNode fromStream(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
		return new XNode(factory.newDocumentBuilder().parse(inputStream));
	}

}
