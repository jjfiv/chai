package ciir.jfoley.chai.string;

import ciir.jfoley.chai.collections.list.IntList;
import ciir.jfoley.chai.fn.TransformFn;
import gnu.trove.list.array.TCharArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author jfoley.
 */
public class StrUtil {
  @Nonnull
  public static String removeBetween(String input, String start, String end) {
    StringBuilder text = new StringBuilder();
    int lastPos = 0;
    while(true) {
      int startPos = input.indexOf(start, lastPos);
      if(startPos == -1) break;
      int endPos = input.indexOf(end, startPos+start.length());
      if(endPos == -1) break;
      endPos += end.length();
      text.append(input.substring(lastPos, startPos));
      lastPos = endPos;
    }
    text.append(input.substring(lastPos));
    return text.toString();
  }

  @Nonnull
  public static String removeBetweenNested(String input, String start, String end) {
    StringBuilder text = new StringBuilder();
    int lastPos = 0;
    while(true) {
      int startPos = input.indexOf(start, lastPos);
      if(startPos == -1) break;
      int endPos = input.indexOf(end, startPos+start.length());
      if(endPos == -1) break;

      // check for nesting; remove largest matching start,end sequence
      while(true) {
        int nextStartPos = input.indexOf(start, startPos + start.length());
        if(nextStartPos == -1 || nextStartPos > endPos) {
          break;
        }
        int nextEndPos = input.indexOf(end, endPos+end.length());
        if(nextEndPos == -1) break;
        endPos = nextEndPos;
      }

      endPos += end.length();
      text.append(input.substring(lastPos, startPos));
      lastPos = endPos;
    }
    text.append(input.substring(lastPos));
    return text.toString();
  }

  /**
   * Calls transform on every string that exists between patterns start and end on input, and returns the result.
   */
  @Nonnull
  public static String transformRecursively(String input, Pattern start, Pattern end, Transform fn, boolean inclusive) {
    StringBuilder text = new StringBuilder();
    int lastPos = 0;

    boolean hasNested = false;
    while(true) {
      Match startMatch = Match.find(input, start, lastPos);
      if(startMatch == null) break;
      Match endMatch = Match.find(input, end, startMatch.end);
      if(endMatch == null) break;

      // check for nesting; do inner-most computation first
      while(true) {
        Match nextStartMatch = Match.find(input, start, startMatch.end);
        if(nextStartMatch == null || nextStartMatch.begin > endMatch.begin) {
          break;
        }
        hasNested = true;
        startMatch = nextStartMatch;
      }

      text.append(input.substring(lastPos, startMatch.begin));
      if(inclusive) {
        text.append(fn.transform(substr(input, startMatch.begin, endMatch.end)));
      } else {
        text.append(fn.transform(substr(input, startMatch.end, endMatch.begin)));
      }
      lastPos = endMatch.end;
    }
    text.append(input.substring(lastPos));

    // go again to grab the outer ones
    if(hasNested) {
      return transformRecursively(text.toString(), start, end, fn, inclusive);
    }
    return text.toString();
  }

  public static String substr(String what, int begin, int end) {
    if(begin > end) {
      return "";
    }
    return what.substring(Math.max(0, begin), Math.min(end, what.length()-1));
  }

  /**
   * Calls transform on every string that exists between patterns start and end on input, and returns the result.
   * Exclusive of the matching patterns
   */
  @Nonnull
  public static String transformBetween(String input, Pattern start, Pattern end, Transform transform) {
    return transformRecursively(input, start, end, transform, false);
  }

  /**
   * Calls transform on every string that exists between patterns start and end on input, and returns the result.
   * Inclusive of the matching patterns
   */
  @Nonnull
  public static String transformInclusive(String input, Pattern start, Pattern end, Transform transform) {
    return transformRecursively(input, start, end, transform, true);
  }

  @Nonnull
  public static String removeBetween(String input, Pattern start, Pattern end) {
    StringBuilder text = new StringBuilder();
    int lastPos = 0;
    while(true) {
      Match startMatch = Match.find(input, start, lastPos);
      if(startMatch == null) break;
      Match endMatch = Match.find(input, end, startMatch.end);
      if(endMatch == null) break;
      text.append(input.substring(lastPos, startMatch.begin));
      lastPos = endMatch.end;
    }
    text.append(input.substring(lastPos));
    return text.toString();
  }

  @Nonnull
  public static String takeBefore(String input, String delim) {
    int pos = input.indexOf(delim);
    if(pos == -1) {
      return input;
    }
    return input.substring(0, pos);
  }
  @Nonnull
  public static String takeBeforeLast(String input, String delim) {
    int pos = input.lastIndexOf(delim);
    if(pos == -1) {
      return input;
    }
    return input.substring(0, pos);
  }
  @Nonnull
  public static String takeBefore(String input, char delim) {
    int pos = input.indexOf(delim);
    if(pos == -1) {
      return input;
    }
    return input.substring(0, pos);
  }

  @Nonnull
  public static String takeAfter(String input, String delim) {
    int pos = input.indexOf(delim);
    if(pos == -1) {
      return input;
    }
    return input.substring(pos + delim.length());
  }

  @Nonnull
  public static String takeBetween(String input, String prefix, String suffix) {
    return takeAfter(takeBefore(input, suffix), prefix);
  }

  @Nonnull
  public static String preview(String input, int len) {
    input = compactSpaces(input);
    if(input.length() < len) {
      return input;
    } else {
      return input.substring(0, len-2)+"..";
    }
  }

  @Nonnull
  public static String firstWord(String text) {
    for(int i=0; i<text.length(); i++) {
      if(Character.isWhitespace(text.charAt(i)))
        return text.substring(0,i);
    }
    return text;
  }

  public static boolean looksLikeInt(String str, int numDigits) {
    if(str.isEmpty()) return false;
    if(str.length() > numDigits)
      return false;
    for(char c : str.toCharArray()) {
      if(!Character.isDigit(c))
        return false;
    }
    return true;
  }
  private static final int numDigitsMaxInt = Integer.toString(Integer.MAX_VALUE).length();

  public static boolean looksLikeInt(String str) {
    return looksLikeInt(str, numDigitsMaxInt);
  }

  @Nonnull
  public static String removeSpaces(String input) {
    StringBuilder output = new StringBuilder();
    for(char c : input.toCharArray()) {
      if(Character.isWhitespace(c))
        continue;
      output.append(c);
    }
    return output.toString();
  }

  @Nonnull
  public static String filterToAscii(String input) {
    StringBuilder ascii = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      if (input.codePointAt(i) <= 127) {
        ascii.append(input.charAt(i));
      }
    }
    return ascii.toString();
  }

  public static boolean isAscii(String input) {
    for (int i = 0; i < input.length(); i++) {
      if (input.codePointAt(i) > 127) {
        return false;
      }
    }
    return true;
  }

  /** Simplify input string in terms of spaces; all space characters -&gt; ' ' and a maximum width of 1 space. */
  @Nonnull
  public static String compactSpaces(CharSequence input) {
    StringBuilder sb = new StringBuilder();
    boolean lastWasSpace = true;
    for (int i = 0; i < input.length(); i++) {
      char ch = input.charAt(i);
      if(Character.isWhitespace(ch)) {
        if(lastWasSpace) continue;
        sb.append(' ');
        lastWasSpace = true;
        continue;
      }
      lastWasSpace = false;
      sb.append(ch);
    }
    if(lastWasSpace) {
      return sb.toString().trim();
    }
    return sb.toString();
  }

  /** Remove ending from input string */
  @Nonnull
  public static String removeBack(String input, String suffix) {
    if(!input.endsWith(suffix)) return input;
    return input.substring(0, input.length() - suffix.length());
  }

  /** Remove ending from input string */
  @Nonnull
  public static String removeBack(String input, int length) {
    return input.substring(0, input.length() - length);
  }

  /** Remove prefix from input string */
  public static String removeFront(String input, String prefix) {
    if(!input.startsWith(prefix)) return input;
    return input.substring(prefix.length());
  }

  /** Remove prefix and suffix from input string */
  @Nonnull
  public static String removeSurrounding(String input, String prefix, String suffix) {
    if(!input.endsWith(suffix)) return removeFront(input, prefix);
    if(!input.startsWith(prefix)) return removeBack(input, suffix);
    return input.substring(prefix.length(), input.length() - suffix.length());
  }

  public static boolean containsAscii(String title) {
    for (int i = 0; i < title.length(); i++) {
      int code = title.codePointAt(i);
      if(code < 128) {
        return true;
      }
    }
    return false;
  }

  @Nonnull
  public static String indent(String message, String tabChars) {
    StringBuilder sb = new StringBuilder();
    for (String line : message.split("\n")) {
      sb.append(tabChars).append(line).append('\n');
    }
    return sb.toString();
  }

  public static String takeAfterLast(String input, char pattern) {
    int index = input.lastIndexOf(pattern);
    if(index < 0) return input;
    return input.substring(index+1);
  }

  public static CharSequence replaceAny(String matching, String withWhat, String expr) {
    StringBuilder output = new StringBuilder();
    char[] original = expr.toCharArray();
    for (char ch : original) {
      if (matching.indexOf(ch) >= 0) {
        output.append(withWhat);
      } else {
        output.append(ch);
      }
    }
    return output;
  }

  public static Long longFromDigits(CharSequence input) {
    TCharArrayList digits = new TCharArrayList();
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if(Character.isDigit(c)) {
        digits.add(c);
        for (int j = i+1; j < input.length(); j++) {
          final char nc = input.charAt(j);
          if(Character.isDigit(nc)) {
            digits.add(nc);
          } else {
            break;
          }
        }
        break;
      }
    }
    if(digits.isEmpty()) {
      return null;
    }
    return Long.parseLong(new String(digits.toArray()));
  }

  public static boolean containsLetterOrDigit(CharSequence input) {
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if(Character.isLetterOrDigit(c)) {
        return true;
      }
    }
    return false;
  }

  public static String capitalize(String token) {
    if(token.length() == 0) return "";
    char cap = Character.toUpperCase(token.charAt(0));
    if(token.length() == 1) return Character.toString(cap);
    return cap + token.substring(1);
  }

  public interface Transform extends TransformFn<String,String> { }

  @Nonnull
  public static String[] pretendTokenize(@Nonnull String input) {
    String cleaned = input
      .toLowerCase()
      .replaceAll("<script[^>]*>[^<]*</script>", " ")
      .replaceAll("<style[^>]*>[^<]*</style>", " ")
      .replaceAll("<!--.*-->", "")
      .replaceAll("&nbsp;", " ")
      .replaceAll("<[^>]*>", " ")
      .replaceAll("\\p{Punct}", " ")
      .replace(']', ' ')
      .replace('?', ' ')
      .replaceAll("^\\p{Alnum}", " ");
    return StrUtil.filterToAscii(cleaned).split("\\s+");
  }

  public static boolean truthy(@Nullable CharSequence seq) {
    return seq != null && seq.length() > 0;
  }
  public static boolean falsy(@Nullable CharSequence seq) {
    return !truthy(seq);
  }

  @Nonnull
	public static String join(List<? extends CharSequence> items, CharSequence delimiter) {
    if(items.size() == 1) { return items.get(0).toString(); }
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < items.size(); i++) {
			if(i != 0) sb.append(delimiter);
			sb.append(items.get(i));
		}
		return sb.toString();
	}
  @Nonnull
  public static String join(List<? extends CharSequence> items) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < items.size(); i++) {
      if(i != 0) sb.append(' ');
      sb.append(items.get(i));
    }
    return sb.toString();
  }

  /**
   * Copy the unicode code points out of a string and into a list.
   * @param input a string.
   * @return a list of unicode integer code points.
   */
  @Nonnull
  public static IntList codePoints(String input) {
    IntList unicode = new IntList();

    int length = input.length();
    for (int offset = 0; offset < length;) {
      int codePoint = input.codePointAt(offset);
      offset += Character.charCount(codePoint);

      unicode.add(codePoint);
    }

    return unicode;
  }

  private static Map<String,String> quoteMap = new HashMap<>();
  static {
    // http://en.wikipedia.org/wiki/Quotation_mark
    quoteMap.put("\u2018", "'");
    quoteMap.put("\u201a", "'");
    quoteMap.put("\u2019", "'");
    quoteMap.put("\u201c", "\"");
    quoteMap.put("\u201d", "\"");
    quoteMap.put("\u201e", "\"");

    // convert <<,>> to "
    quoteMap.put("\u00ab", "\"");
    quoteMap.put("\u00bb", "\"");
    // convert <,> to "
    quoteMap.put("\u2039", "\"");
    quoteMap.put("\u203a", "\"");

    // corner marks to single-quotes
    quoteMap.put("\u300c", "'");
    quoteMap.put("\u300d", "'");
  }
  @Nonnull
  public static String replaceUnicodeQuotes(String input) {
    String result = input;
    for (Map.Entry<String, String> kv : quoteMap.entrySet()) {
      result = result.replaceAll(kv.getKey(), kv.getValue());
    }
    return result;
  }

  @Nonnull
  public static String slice(@Nonnull String input, int start) {
    int begin = Math.min(input.length(), Math.max(0, start));
    return input.substring(begin);
  }

  @Nonnull
  public static String slice(@Nonnull String input, int start, int end) {
    int begin = Math.min(input.length(), Math.max(0, start));
    int size = Math.min(input.length(), (end - begin));
    return input.substring(begin, size);
  }

  private static Pattern diacriticMarks = Pattern.compile("\\p{InCombiningDiacriticalMarks}");
  /**
   * Convert all accents and tildes and other marks to normalized text for more aggressive matching.
   * @param input the input pattern to convert
   * @return the input without any marks.
   */
  @Nonnull
  public static String collapseSpecialMarks(CharSequence input) {
    String normed = replaceUnicodeQuotes(Normalizer.normalize(input, Normalizer.Form.NFD));
    return diacriticMarks.matcher(normed).replaceAll("");
  }

  public static List<String> wordWrap(String input, int width) {
    ArrayList<String> output = new ArrayList<>();
    final char[] chars = input.toCharArray();
    final int N = input.length();

    if(N < width) {
      return Arrays.asList(input.split("\n"));
    }
    int prevLine = 0;
    while(true) {
      int newline = -1;
      int maxspace = -1;
      for (int i = 0; i <= Math.min(N-prevLine-1, width); i++) {
        final char c = chars[prevLine + i];
        if(c == '\n') {
          newline = i;
          break;
        }
        if(Character.isWhitespace(c) || c == '-') {
          maxspace = Math.max(prevLine+i, maxspace);
        }
      }

      // handle newlines:
      if(newline >= prevLine) {
        output.add(new String(chars, prevLine, newline));
        prevLine = newline + 1;
        continue;
      }

      int guess = prevLine+width;
      //System.out.println(prevLine+", "+guess+", "+chars.length);
      int split = -1;
      if(guess >= chars.length) { // end of string.
        split = chars.length;
      } else {
        split = maxspace;
      }

      Character hyphen = null;
      if(split == -1 && chars[split+1] != ' ') {
        hyphen = '-';
        split = guess-1;
      }
      StringBuilder sb = new StringBuilder();
      sb.append(chars, prevLine, split-prevLine);
      if(hyphen != null) {
        sb.append(hyphen);
        prevLine = split;
      } else {
        prevLine = split + 1;
      }

      output.add(sb.toString());
      if(split == chars.length) break;
    }
    return output;
  }
}
