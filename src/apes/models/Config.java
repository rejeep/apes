package apes.models;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;


/**
 * <p>
 * This class handles the user specific configuration file. This class uses the
 * Singleton pattern. That means that if you want to use this class you should
 * create an object like this:
 * 
 * <pre>
 * Config config = Config.getInstance();
 * </pre>
 * 
 * </p>
 * <p>
 * An option in the configuration file should consist of: At least one character
 * from a-z (underscore may separate words), then an arbitrary number of spaces,
 * then an equal sign, then again an arbitrary number of spaces and then the
 * value optionally in quotes.
 * </p>
 * <p>
 * Here are some options that are valid.
 * </p>
 * <ul>
 * <li>option="value"</li>
 * <li>option=value</li>
 * <li>option = "value"</li>
 * <li>option = value</li>
 * <li>option_two = "value"</li>
 * <li>option_two=value</li>
 * </ul>
 * And lines starting with a <code>#</code> is a comment.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Config extends ApesConfiguration
{
  /**
   * An instance of this class.
   */
  private static Config instance = null;

  /**
   * Contains all option types. Examples: { "size" => Type.INTEGER, "maximized"
   * => Type.BOOLEAN }
   */
  private Map<String, Type> types;

  /**
   * Different types an option can be.
   */
  public enum Type
  {
    BOOLEAN, STRING, INTEGER
  };

  /**
   * Creates a new <code>Config</code> instance. A default file name is chosen.
   * Use {@link Config#setFile setFile} to override this.
   */
  private Config()
  {
    // Initialize types map.
    types = new HashMap<String, Type>();

    // Default settings.
    addOption("volume", "50", Type.INTEGER);
    addOption("volume_label_format", "%v %", Type.STRING);
    addOption("frame_width", "600", Type.INTEGER);
    addOption("frame_height", "400", Type.INTEGER);
    addOption("graph_width", "500", Type.INTEGER);
    addOption("graph_height", "300", Type.INTEGER);
    addOption("maximized", "true", Type.BOOLEAN);
    addOption("undo", "10", Type.INTEGER);
    addOption("language", "en", Type.STRING);
    addOption("plugin_path", "build/apes/plugins", Type.STRING);
    addOption("color_play", "#FFFFFF", Type.STRING);
    addOption("color_graph", "#000000", Type.STRING);
    addOption("color_selection", "#0000FF", Type.STRING);
    addOption("color_lines", "#FFFF00", Type.STRING);
    addOption("color_background", "#939391", Type.STRING);
    addOption("color_dots", "#0000FF", Type.STRING);
    addOption("color_ruler", "#000000", Type.STRING);
    addOption("color_status", "#FF0000", Type.STRING);
    addOption("ruler_width", "6", Type.INTEGER);
    addOption("gui_error_messages", "true", Type.BOOLEAN);
    addOption("wind", "20", Type.INTEGER);
    addOption("close_confirmation", "true", Type.BOOLEAN);
  }

  /**
   * Get the value for <code>key</code>.
   * 
   * @param key
   *          The configuration key.
   * @return the value for <code>key</code>.
   */
  public String getOption(String key)
  {
    return options.get(key);
  }

  /**
   * Same as {@link Config#getOption getOption} except that the value will be
   * true or false instead of "true" and "false".
   * 
   * @param key
   *          The configuration key.
   * @return the value for <code>key</code> casted to a boolean. "true" option
   *         will return true. Everything else will return false.
   */
  public boolean getBooleanOption(String key)
  {
    return "true".equals(getOption(key));
  }

  /**
   * Same as {@link Config#getOption getOption} except that the value will be
   * not be a string, but an integer.
   * 
   * @param key
   *          The configuration key.
   * @return the value for <code>key</code> casted to an integer.
   * @exception NumberFormatException
   *              if there was no value to key.
   */
  public int getIntOption(String key) throws NumberFormatException
  {
    return new Integer(getOption(key));
  }

  /**
   * Adds an option.
   * 
   * @param key
   *          The option key.
   * @param default_value
   *          The default value.
   * @param type
   *          The type (string, integer, boolean).
   */
  public void addOption(String key, String default_value, Type type)
  {
    options.put(key, default_value);

    types.put(key, type);
  }

  /**
   * Return the types map.
   * 
   * @param key
   *          The configuration key.
   * @return The map containing all types.
   */
  public Type getType(String key)
  {
    return types.get(key);
  }

  @Override
  public String getConfigurationFileName()
  {
    return "config";
  }
  
  @Override
  public void match(String line, Matcher matcher)
  {
    options.put(matcher.group(1), matcher.group(3));
  }

  /**
   * Will return an instance of this class.
   * 
   * @return An instance of this class.
   */
  public static Config getInstance()
  {
    if(instance == null)
    {
      instance = new Config();
    }

    return instance;
  }
}
