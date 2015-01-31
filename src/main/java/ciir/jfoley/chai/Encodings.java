package ciir.jfoley.chai;

import ciir.jfoley.chai.codec.Codec;
import ciir.jfoley.chai.errors.FatalError;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
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

	public static Codec<ByteBuffer, String> UTF8Codec = new Codec<ByteBuffer, String>() {
		@Override
		public String wrap(ByteBuffer bytes) {
			return UTF8.decode(bytes).toString();
		}

		@Override
		public ByteBuffer unwrap(String s) {
			return UTF8.encode(s);
		}
	};

	public static Codec<Integer, String> IntAsDecimal = new Codec<Integer, String>() {
		@Override
		public String wrap(Integer integer) {
			return integer.toString();
		}

		@Override
		public Integer unwrap(String s) {
			return Integer.parseInt(s);
		}
	};
}
