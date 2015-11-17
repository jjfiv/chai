package ciir.jfoley.chai.lang;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jfoley
 */
public abstract class UserData {
  protected Map<Class<?>, Object> userData = new HashMap<>();

  public <T> T setUserData(@Nonnull Class<T> klazz, @Nonnull T instance) {
    Object prev = userData.put(klazz, instance);
    if(prev == null) return null;
    return getAs(klazz, instance);
  }

  @Nonnull
  public <T> T getUserData(@Nonnull Class<T> klazz) {
    T obj = getUserData(klazz, null);
    if(obj == null) throw new NullPointerException("Couldn't find userdata for class="+klazz);
    return obj;
  }
  public <T> T getUserData(@Nonnull Class<T> klazz, T ifNotFound) {
    return getAs(klazz, userData.getOrDefault(klazz, ifNotFound));
  }

  @SuppressWarnings("unchecked")
  public static <T> T getAs(Class<T> klazz, Object any) {
    Class<?> forAny = any.getClass();
    if(klazz.isAssignableFrom(forAny)) {
      return (T) any;
    } else {
      throw new ClassCastException("Could not assign "+forAny+" to "+klazz+" for object="+any);
    }
  }

}
