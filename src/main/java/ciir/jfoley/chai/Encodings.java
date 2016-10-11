package ciir.jfoley.chai;

import ciir.jfoley.chai.codec.Codec;
import ciir.jfoley.chai.errors.FatalError;
import ciir.jfoley.chai.lang.Module;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author jfoley.
 */
public final class Encodings extends Module {
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
			if(bytes == null) return null;
			return UTF8.decode(bytes).toString();
		}

		@Override
		public ByteBuffer unwrap(String s) {
			if(s == null) return null;
			return UTF8.encode(s);
		}
	};

	public static Codec<Integer, String> IntAsDecimal = new Codec<Integer, String>() {
		@Override
		public String wrap(Integer integer) {
			if(integer == null) return null;
			return integer.toString();
		}

		@Override
		public Integer unwrap(String s) {
			if(s == null) return null;
			return Integer.parseInt(s);
		}
	};

	public static <T> Codec<T,T> getIdentityCodec() {
		return new Codec<T, T>() {
			@Override
			public T wrap(T t) {
				return t;
			}

			@Override
			public T unwrap(T t) {
				return t;
			}
		};
	}
}
