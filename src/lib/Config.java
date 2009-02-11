package src.lib;

import java.io.File;
import java.util.Map;

/**
 * This class handles the user specific configuration file. This class
 * uses the Singleton pattern. That means that if you want to use this
 * class you create an object like this:
 * <pre>Config config = Config.getInstance();</pre>
 *
 * The configuration file should be in the format:
 * <pre>option=value</pre>
 *
 * And lines starting with a <code>#</code> is a comment.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Config
{
  /**
   * An instance of this class.
   */
  private static Config instance = null;

  /**
   * The configuration file.
   */
  private File file;

  /**
   * Contains all options. Key is the option and value is the option
   * value. Example: { "size" => "100" }
   */
  private Map<String, String> options;

  private Config()
  {

  }

  /**
   * Will return an instance of this class.
   *
   * @return An instance of this class.
   */
  public static Config getInstance()
  {
    if( instance == null )
    {
      instance = new Config();
    }

    return instance;
  }
}