package apes.models;

import javax.swing.KeyStroke;

/**
 * <p>
 * This class handles user specific key bindings. This class uses the Singleton
 * pattern. That means that if you want to use this class you should create an
 * object like this:
 * 
 * <pre>
 * Config config = Config.getInstance();
 * </pre>
 * 
 * </p>
 * <p>
 * An keybinding in the file should consist of: At least one character from a-z
 * (underscore may separate words), then an arbitrary number of spaces, then an
 * equal sign, then again an arbitrary number of spaces and then the value
 * optionally in quotes.
 * </p>
 * <p>
 * Here are some valid key bindings.
 * </p>
 * <ul>
 * <li>play="alt P"</li>
 * <li>stop=alt V</li>
 * <li>undo = "control Z"</li>
 * <li>save = control S</li>
 * </ul>
 * <p>
 * Lines starting with a <code>#</code> is a comment.
 * </p>
 * 
 * <p>See {@link KeyStroke#getKeyStroke(String)} for information on how to write the key bindings.</p>
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class KeyBinding extends ApesConfiguration
{
  /**
   * An instance of this class.
   */
  private static KeyBinding instance = null;

  /**
   * Private so that it only can be created through getInstance().
   */
  private KeyBinding()
  {
    options.put("open", "control O");
    options.put("save", "control S");
    options.put("save_as", "control W");
    options.put("export", "control E");
    options.put("quit", "control Q");
    options.put("undo", "control Z");
    options.put("redo", "control R");
    options.put("cut", "control X");
    options.put("copy", "control C");
    options.put("paste", "control V");
    options.put("delete", "control D");
    options.put("properties", "control I");
    options.put("plugins", "control P");
    options.put("key_bindings", "control B");
    options.put("tags", "control ");
    options.put("zoom_in", "alt shift I");
    options.put("zoom_out", "alt shift O");
    options.put("zoom_selection", "alt shift S");
    options.put("zoom_reset", "alt shift R");
    options.put("play", "alt P");
    options.put("pause", "alt C");
    options.put("stop", "alt S");
    options.put("forward", "alt F");
    options.put("backward", "alt B");
  }

  /**
   * Returns the key binding to <code>key</code>.
   * 
   * @param key The key.
   * @return The key binding.
   */
  public String get(String key)
  {
    return options.get(key);
  }

  /**
   * Will return an instance of this class.
   * 
   * @return An instance of this class.
   */
  public static KeyBinding getInstance()
  {
    if(instance == null)
    {
      instance = new KeyBinding();
    }

    return instance;
  }

  @Override
  public String getConfigurationFileName()
  {
    return "keybindings";
  }
}
