package apes.lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeMap;

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
   * Contains all option types. Examples:
   *
   * { "size" => Type.INTEGER, "maximized" => Type.BOOLEAN }
   */
  private Map<String, Type> types;

  /**
   * Regexp to match an option in the configuration file. It uses
   * grouping to extract the key and value.
   */
  private static final String OPTION_REGEXP = "^([a-z_]+)\\s*=\\s*([\"]{0,1})(.*)\\2$";

  /**
   * Different types an option can be.
   */
  public enum Type { BOOLEAN, STRING, INTEGER };

  /**
   * Creates a new <code>Config</code> instance. A default file name
   * is chosen. Use {@link Config#setFile setFile} to override this.
   */
  private Config()
  {
    // Default configuration file.
    setFilePath( System.getProperty( "user.home" ) + File.separator + ".apes" );

    // Initialize options map.
    options = new TreeMap<String, String>();

    // Initialize types map.
    types = new HashMap<String, Type>();

    // Default settings.
    addOption( "volume", "50", Type.INTEGER );
    addOption( "volume_label_format", "%v %", Type.STRING );
    addOption( "frame_width", "600", Type.INTEGER );
    addOption( "frame_height", "400", Type.INTEGER );
    addOption( "graph_width", "500", Type.INTEGER );
    addOption( "graph_height", "300", Type.INTEGER );
    addOption( "maximized", "true", Type.BOOLEAN );
    addOption( "undo", "10", Type.INTEGER );
    addOption( "language", "en", Type.STRING );
    addOption( "color_play", "#FF00FF", Type.STRING );
    addOption( "color_graph", "#00FF00", Type.STRING );
    addOption( "color_selection", "#0000FF", Type.STRING );
    addOption( "color_lines", "#FFFF00", Type.STRING );
    addOption( "color_background", "#FF0000", Type.STRING );
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

        scanner.close();
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
   * Adds an option.
   *
   * @param key The option key.
   * @param default_value The default value.
   * @param type The type (string, integer, boolean).
   */
  public void addOption( String key, String default_value, Type type )
  {
    options.put( key, default_value );

    types.put( key, type );
  }

  /**
   * Return the types map.
   *
   * @param key The configuration key.
   * @return The map containing all types.
   */
  public Type getType( String key )
  {
    return types.get( key );
  }

  /**
   * Return the options map.
   *
   * @return The map containing all options.
   */
  public Map<String, String> getOptions()
  {
    return this.options;
  }

  /**
   * Save all options to the configuration files.
   */
  public void save()
  {
    try
    {
      // Read in the whole file. This must be done because a file can
      // not be written to while read from.
      List<String> lines = new ArrayList<String>();
      Scanner scanner = new Scanner( file );
      while( scanner.hasNextLine() )
      {
        lines.add(scanner.nextLine());
      }
      scanner.close();


      PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter( file ) ) );
      Set<String> added = new HashSet<String>();
      Pattern pattern = Pattern.compile( OPTION_REGEXP );
      Matcher matcher;
      String key;

      for( String line : lines )
      {
        matcher = pattern.matcher( line );

        if( matcher.matches() )
        {
          key = matcher.group( 1 );

          out.write( toOption( key ) );

          added.add( key );
        }
        else
        {
          out.write( line );
        }

        out.println();
      }

      // Add options that where not in the file already.
      for( String option : options.keySet() )
      {
        if( !added.contains( option ) )
        {
          out.write( toOption( option ) );

          out.println();
        }
      }

      out.close();
    }
    catch( FileNotFoundException e )
    {
      e.printStackTrace();
    }
    catch( IOException e )
    {
      e.printStackTrace();
    }
  }

  /**
   * Returns a string of an option.
   *
   * @param key The key option.
   * @return The option as a string (key = value).
   */
  private String toOption( String key )
  {
    return key + " = " + options.get( key );
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