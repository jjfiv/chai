package ciir.jfoley.chai.io.compress;

import ciir.jfoley.chai.io.CompressionCodec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author jfoley.
 */
public abstract class ReflectionCodec implements CompressionCodec.Impl {
	Constructor<? extends InputStream> cachedInputConstructor = null;
	Constructor<? extends OutputStream> cachedOutputConstructor = null;

	public abstract String getInputClassName();
	public abstract String getOutputClassName();

	@SuppressWarnings("unchecked")
	public Constructor<? extends InputStream> getInputConstructor() {
		if(cachedInputConstructor == null) {
			try {
				cachedInputConstructor = (Constructor<? extends InputStream>) Class.forName(getInputClassName()).getDeclaredConstructor(InputStream.class);
			} catch (ClassNotFoundException | NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
		return cachedInputConstructor;
	}
	@SuppressWarnings("unchecked")
	public Constructor<? extends OutputStream> getOutputConstructor() {
		if(cachedOutputConstructor == null) {
			try {
				cachedOutputConstructor = (Constructor<? extends OutputStream>) Class.forName(getOutputClassName()).getDeclaredConstructor(OutputStream.class);
			} catch (ClassNotFoundException | NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
		return cachedOutputConstructor;
	}


	@Override
	public InputStream openReader(InputStream fp) throws IOException {
		try {
			return getInputConstructor().newInstance(fp);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public OutputStream openWriter(OutputStream fp) throws IOException {
		try {
			return getOutputConstructor().newInstance(fp);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
