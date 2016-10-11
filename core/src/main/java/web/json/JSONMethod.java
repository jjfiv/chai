package web.json;

import org.lemurproject.galago.utility.Parameters;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author jfoley.
 */
public interface JSONMethod {
  @Nullable
  Parameters process(@Nonnull Parameters jreq) throws Exception;
}
