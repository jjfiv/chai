package ciir.jfoley.chai.string;

import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Pattern;

import static ciir.jfoley.chai.string.StrUtil.*;
import static org.junit.Assert.*;

public class StrUtilTest {

  @Test
  public void testRemoveBetween() throws Exception {
    String testInput = "This would be a way of <!-- something here -->simply removing html comments...";
    assertEquals("This would be a way of simply removing html comments...", removeBetween(testInput, "<!--", "-->"));
  }

  @Test
  public void testTake() {
    assertEquals("contents", takeBetween("Not useful <text>contents</text> is here!", "<text>", "</text>"));
  }

  @Test
  public void testRemoveBetweenNested() throws Exception {
    String testInput = "nested/* /* nested comments are */ difficult */comments";
    assertEquals("nestedcomments", removeBetweenNested(testInput, "/*", "*/"));
  }

  @Test
  public void testTransformBetween() throws Exception {
    String testInput = "this is a $(macro $(thing))";
    String testOutput = transformBetween(testInput, Pattern.compile("\\$\\("), Pattern.compile("\\)"), input -> {
      if (input.equals("thing")) {
        return "bar";
      } else if (input.equals("macro bar")) {
        return "foo";
      }
      return "";
    });

    assertEquals("this is a foo", testOutput);
  }

  @Test
  public void testTakeBefore() throws Exception {
    assertEquals("foo", takeBefore("foo bar", " "));
  }

  @Test
  public void testTakeAfter() throws Exception {
    assertEquals("bar", takeAfter("foo bar", " "));
  }

  @Test
  public void testPreview() throws Exception {
    assertEquals("This is the way..", preview("This is the way it has always been.", 17));
    assertEquals("Not clipped", preview("Not clipped", 17));
  }

  @Test
  public void testFirstWord() throws Exception {
    assertEquals("foo", firstWord("foo bar"));
    assertEquals("foo", firstWord("foo\tbar"));
    assertEquals("foo", firstWord("foo"));
  }

  @Test
  public void testLooksLikeInt() throws Exception {
    assertTrue(looksLikeInt("123", 3));
    assertFalse(looksLikeInt("123456", 3)); // too long
    assertFalse(looksLikeInt("1234", 3)); // too long
  }

  @Test
  public void testRemoveSpaces() throws Exception {
    assertEquals("foobar", removeSpaces("\nfoo\tbar "));
  }

  @Test
  public void testFilterToAscii() throws Exception {
    assertEquals("foobar", filterToAscii("foo\u2013bar"));
  }

  @Test
  public void testIsAscii() throws Exception {
    assertFalse(isAscii("foo\u2013bar"));
    assertTrue(isAscii("foobar~"));
  }

  @Test
  public void testCompactSpaces() {
    assertEquals("foo bar", compactSpaces(" foo \tbar\t \n"));
  }

  @Test
  public void removePrefixAndOrSuffix() {
    // removeBack
    assertEquals("Hello", removeBack("Hello.html", ".html"));
    assertEquals("foo", removeBack("foo.foo", ".foo"));

    // removeFront
    assertEquals("foo>", removeFront("<foo>", "<"));
    assertEquals(">", removeFront("<foo>", "<foo"));

    //remove Surrounding
    // removeFront
    assertEquals("foo", removeSurrounding("<foo>", "<", ">"));
    assertEquals("foo", removeSurrounding("\"foo\"", "\"", "\""));
  }

  @Test
  public void testIndent() {
    String data = "the\ndog";
    assertEquals("\tthe\n\tdog\n", indent(data, "\t"));
  }

  @Test
  public void testCodePoints() {
    String sample = "\u201e\u201c\u201awell";
    assertEquals(Arrays.asList(0x201e, 0x201c, 0x201a, 119, 101, 108, 108), codePoints(sample));
  }

  @Test
  public void testReplaceUnicodeQuotes() {
    String sample = "\u201e\u201c\u201awell";
    assertEquals("\"\"'well", replaceUnicodeQuotes(sample));
  }
}