package edu.umass.cs.ciir.jfoley.chai.errors;

/**
 * @author jfoley
 */
public class NotHandledNow extends IllegalArgumentException {
  public NotHandledNow(String where, String method) {
    this("'"+method+"' not handled now for '"+where+"'");
  }
  public NotHandledNow(String msg) {
    super(msg);
  }
}

