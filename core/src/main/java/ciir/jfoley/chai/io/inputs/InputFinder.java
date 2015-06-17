package ciir.jfoley.chai.io.inputs;

import ciir.jfoley.chai.io.Directory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * @author jfoley
 */
public class InputFinder {
  public static final Logger logger = Logger.getLogger(InputFinder.class.getName());
  public final List<FileHandler> handlers;

  public static List<FileHandler> defaultHandlers = new Vector<>();
  static {
    defaultHandlers.add(new ZipInputHandler());
    defaultHandlers.add(new TarInputHandler());
  }

  public static InputFinder Default() {
    return new InputFinder(defaultHandlers);
  }

  public InputFinder(List<FileHandler> handlers) {
    this.handlers = handlers;
  }

  public interface FileHandler {
    boolean matches(File input);
    InputContainer getContainer(File input) throws IOException;
  }

  public List<? extends InputContainer> findAllInputs(Directory dir) throws IOException {
    ArrayList<InputContainer> output = new ArrayList<>();
    for (File child : dir.recursiveChildren()) {
      output.add(asInputContainer(child));
    }
    return output;
  }

  public InputContainer asInputContainer(File child) throws IOException {
    for (FileHandler handler : this.handlers) {
      if(handler.matches(child)) {
        return handler.getContainer(child);
      }
    }
    return new SingletonInputContainer(child.getName(), new FileInput(child));
  }

}
