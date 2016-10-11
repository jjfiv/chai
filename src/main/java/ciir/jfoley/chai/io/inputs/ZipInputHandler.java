package ciir.jfoley.chai.io.inputs;

import ciir.jfoley.chai.io.archive.ZipArchive;

import java.io.File;
import java.io.IOException;

/**
 * @author jfoley
 */
public class ZipInputHandler implements InputFinder.FileHandler {
  @Override
  public boolean matches(File input) {
    return input.getName().endsWith(".zip");
  }

  @Override
  public InputContainer getContainer(File input) throws IOException {
    return ZipArchive.open(input);
  }
}
