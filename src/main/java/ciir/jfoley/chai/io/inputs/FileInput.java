package ciir.jfoley.chai.io.inputs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * @author jfoley
 */
public class FileInput implements InputStreamable {
  public final File input;

  public FileInput(String input) {
    this.input = new File(input);
  }

  public FileInput(File input) {
    this.input = input;
  }

  @Override
  public String getName() {
    return input.getName();
  }

  @Override
  public InputStream getRawInputStream() throws IOException {
    return Files.newInputStream(input.toPath(), StandardOpenOption.READ);
  }
}
