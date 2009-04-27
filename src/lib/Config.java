package apes.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>This class handles the user specific configuration file. This class
 * uses the Singleton pattern. That means that if you want to use this
 * class you should create an object like this:
 * <pre>Config config = Config.getInstance();</pre></p>
 *
 * <p>An option in the configuration file should consist of: At least one
 * character from a-z, then an arbitrary number of spaces, then an
 * equal sign, then again an arbitrary number of spaces and then the
 * value optionally in quotes.</p>
 *
 * <p>Here are some options that are valid.</p>
 * <ul>
 *   <li>option="value"</li>
 *   <li>option=value</li>
 *   <li>option = "value"</li>
 *   <li>option = value</li>
 *   <li>option_two = "value"</li>
 *   <li>option_two=value</li>
 * </ul>
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

  /**
   * Regexp to match an option in the configuration file. It uses
   * grouping to extract the key and value.
   */
  private static final String OPTION_REGEXP = "^([a-z_]+)\\s*=\\s*([\"]{0,1})(.*)\\2$";

  /**
   * Creates a new <code>Config</code> instance. A default file name
   * is chosen. Use {@link Config#setFile setFile} to override this.
   */
  private Config()
  {
    // Default configuration file.
    setFilePath( System.getProperty( "user.home" ) + File.separator + ".apes" );

    // Initialize options map.
    options = new HashMap<String, String>();

    // Default settings.
    options.put( "volume", "50" );
    options.put( "maximized", "true" );
    options.put( "width", "600" );
    options.put( "height", "400" );
    options.put( "volume_label_format", "%v %" );
    options.put( "graphwidth", "500" );
    options.put( "graphheight", "300" );
  }

  /**
   * Parses {@link Config#file configuration file} and adds all
   * options to {@link Config#options options}.
   */
  public void parse()
  {
    // Only parse if there is any file.
    if( file.isFile() )
    {
      try
      {
        Scanner scanner = new Scanner( file );
        Pattern pattern = Pattern.compile( OPTION_REGEXP );
        Matcher matcher;
        String line;

        // Go through all lines in configuration file.
        while( scanner.hasNextLine() )
        {
          line = scanner.nextLine();
          matcher = pattern.matcher( line );

          // If line matches an option.
          if( matcher.matches() )
          {
            options.put( matcher.group( 1 ), matcher.group( 3 ) );
          }
        }
      }
      catch( FileNotFoundException e )
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * Get the absolute path to the configuration file.
   *
   * @return the absolute path to the configuration file.
   */
  public String getFilePath()
  {
    return file.getAbsolutePath();
  }

  /**
   * Set the configuration file.
   *
   * @param path The absolute path to the file.
   */
  public void setFilePath( String path )
  {
    this.file = new File( path );
  }

  /**
   * Get the value for <code>key</code>.
   *
   * @param key The configuration key.
   * @return the value for <code>key</code>.
   */
  public String getOption( String key )
  {
    return options.get( key );
  }

  /**
   * Same as {@link Config#getOption getOption} except that the value
   * will be true or false instead of "true" and "false".
   *
   * @param key The configuration key.
   * @return the value for <code>key</code> casted to a
   * boolean. "true" option will return true. Everything else will
   * return false.
   */
  public boolean getBooleanOption( String key )
  {
    return "true".equals( getOption( key ) );
  }

  /**
   * Same as {@link Config#getOption getOption} except that the value
   * will be not be a string, but an integer.
   *
   * @param key The configuration key.
   * @return the value for <code>key</code> casted to an integer.
   * @exception NumberFormatException if there was no value to key.
   */
  public int getIntOption( String key ) throws NumberFormatException
  {
    return new Integer( getOption( key ) );
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