package edu.umass.cs.ciir.jfoley.chai;

import edu.umass.cs.ciir.jfoley.chai.errors.FatalError;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * @author jfoley.
 */
public class Encodings {
	public static Charset UTF8 = Charset.defaultCharset();
	public static String encodeURLUTF8(String input) {
		try {
			return URLEncoder.encode(input, UTF8.name());
		} catch (UnsupportedEncodingException e) {
			throw new FatalError(e);
		}
	}
}
