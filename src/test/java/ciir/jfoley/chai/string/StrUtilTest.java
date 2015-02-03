package ciir.jfoley.chai.string;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class StrUtilTest {

  @Test
  public void testRemoveBetween() throws Exception {
    String testInput = "This would be a way of <!-- something here -->simply removing html comments...";
    assertEquals("This would be a way of simply removing html comments...", StrUtil.removeBetween(testInput, "<!--", "-->"));
  }

  @Test
  public void testTake() {
    assertEquals("contents", StrUtil.takeBetween("Not useful <text>contents</text> is here!", "<text>", "</text>"));
  }

  @Test
  public void testRemoveBetweenNested() throws Exception {
    String testInput = "nested/* /* nested comments are */ difficult */comments";
    assertEquals("nestedcomments", StrUtil.removeBetweenNested(testInput, "/*", "*/"));
  }

  @Test
  public void testTransformBetween() throws Exception {
    String testInput = "this is a $(macro $(thing))";
    String testOutput = StrUtil.transformBetween(testInput, Pattern.compile("\\$\\("), Pattern.compile("\\)"), new StrUtil.Transform() {
      @Override
      public String process(String input) {
        if(input.equals("thing")) {
          return "bar";
        } else if(input.equals("macro bar")) {
          return "foo";
        }
        return "";
      }
    });

    assertEquals("this is a foo", testOutput);
  }

  @Test
  public void testTakeBefore() throws Exception {
    assertEquals("foo", StrUtil.takeBefore("foo bar", " "));
  }

  @Test
  public void testTakeAfter() throws Exception {
    assertEquals("bar", StrUtil.takeAfter("foo bar", " "));
  }

  @Test
  public void testPreview() throws Exception {
    assertEquals("This is the way..", StrUtil.preview("This is the way it has always been.", 17));
    assertEquals("Not clipped", StrUtil.preview("Not clipped", 17));
  }

  @Test
  public void testFirstWord() throws Exception {
    assertEquals("foo", StrUtil.firstWord("foo bar"));
    assertEquals("foo", StrUtil.firstWord("foo\tbar"));
    assertEquals("foo", StrUtil.firstWord("foo"));
  }

  @Test
  public void testLooksLikeInt() throws Exception {
    assertTrue(StrUtil.looksLikeInt("123", 3));
    assertFalse(StrUtil.looksLikeInt("123456", 3)); // too long
    assertFalse(StrUtil.looksLikeInt("1234", 3)); // too long
  }

  @Test
  public void testRemoveSpaces() throws Exception {
    assertEquals("foobar", StrUtil.removeSpaces("\nfoo\tbar "));
  }

  @Test
  public void testFilterToAscii() throws Exception {
    assertEquals("foobar", StrUtil.filterToAscii("foo\u2013bar"));
  }

  @Test
  public void testIsAscii() throws Exception {
    assertFalse(StrUtil.isAscii("foo\u2013bar"));
    assertTrue(StrUtil.isAscii("foobar~"));
  }

  @Test
  public void testCompactSpaces() {
    assertEquals("foo bar", StrUtil.compactSpaces(" foo \tbar\t \n"));
  }

  @Test
  public void removePrefixAndOrSuffix() {
    // removeBack
    assertEquals("Hello", StrUtil.removeBack("Hello.html", ".html"));
    assertEquals("foo", StrUtil.removeBack("foo.foo", ".foo"));

    // removeFront
    assertEquals("foo>", StrUtil.removeFront("<foo>", "<"));
    assertEquals(">", StrUtil.removeFront("<foo>", "<foo"));

    //remove Surrounding
    // removeFront
    assertEquals("foo", StrUtil.removeSurrounding("<foo>", "<", ">"));
    assertEquals("foo", StrUtil.removeSurrounding("\"foo\"", "\"", "\""));
  }
}