package ciir.jfoley.chai.string;

import java.util.AbstractList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Now implementing a List&gt;String&lt; of matches for easier iteration.
 * @author jfoley.
 */
public final class Match extends AbstractList<String> {
  public final int begin;
  public final int end;
  public final Matcher matcher;

  public Match(Matcher matcher) {
    this.matcher = matcher;
    begin = matcher.start();
    end = matcher.end();
  }

  public static Match find(String input, Pattern pattern) {
    return find(input, pattern, 0);
  }
  public static Match find(String input, Pattern pattern, int start) {
    Matcher match = pattern.matcher(input);
    if(match.find(start)) {
      return new Match(match);
    }
    return null;
  }

  public String get(String parentStr) {
    return parentStr.substring(begin, end);
  }

  @Override
  public int size() {
    return matcher.groupCount();
  }

  @Override
  public String get(int index) {
    // 0th match is often the whole string, or the whole range.
    return matcher.group(index+1);
  }

  public String firstGroup() {
    return matcher.group(1);
  }
}
