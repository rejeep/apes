package apes.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.ho.yaml.Yaml;

/**
 * <p>This class handles the locale.</p>
 *
 * <p>This class uses the Singleton pattern. That means that if you
 * want to use this class you should create an object like this:
 * <pre>Locale locale = Locale.getInstance();</pre></p>
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Locale
{
  /**
   * An instance of this class.
   */
  private static Locale instance = null;

  /**
   * The active locale.
   */
  private String locale;

  /**
   * Contains the contents of the locale file.
   */
  private Object yaml;

  /**
   * Default path for locale files.
   */
  private String localePath = "locales";

  /**
   * This method exists only so that an object cannot be created of
   * this class without using the singleton pattern.
   */
  private Locale() { }

  /**
   * Get the default locale path.
   *
   * @return The default locale path.
   */
  public String getLocalePath()
  {
    return localePath;
  }

  /**
   * Sets the default locale path.
   *
   * @param localePath to folder which should be the default locale path.
   */
  public void setLocalePath( String localePath )
  {
    this.localePath = localePath;
  }

  /**
   * Returns the name of the active locale.
   *
   * @return The name of the active locale.
   */
  public String getLocale()
  {
    return locale;
  }

  /**
   * Set the locale that should be active now. After the new locale is
   * set the method {@link Locale#load load} must be called so that
   * the new locale file is read.
   *
   * @param newLocale The name of the new locale.
   */
  public void setLocale( String newLocale )
  {
    this.locale = newLocale;
  }

  /**
   * Reads the contents of the locale file. Note that the locale must
   * be set for this method to load the file. Use {@link
   * Locale#setLocale setLocale} to set locale.
   */
  public void load()
  {
    if( locale != null )
    {
      try
      {
        yaml = Yaml.load( new File( localePath + "/" + locale + ".yml" ) );
      }
      catch( FileNotFoundException e )
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * <p>This method returns the value for <code>tag</code> if it
   * exist. A tag is a string that refers to an element in a yaml
   * file. For example is <code>person.name</code> a tag that for this
   * yaml file:</p>
   *
   * <pre>person:
   *  name: "Name"
   *  age: "Age"</pre>
   *
   * <p>would return "Name".</p>
   *
   * @param tag The tag to get the value from.
   * @return The value that corresponds to <code>tag</code>, or null
   * or <code>tag</code> does not exist.
   */
  public String get( String tag )
  {
    Object rest = yaml;
    String result = null;

    try
    {
      for( String key : tag.split( "\\." ) )
      {
        if( ((HashMap)rest).containsKey( key ) )
        {
          rest = ((HashMap)rest).get( key );
        }
      }

      result = (String)rest;
    }
    catch( ClassCastException e ) { }

    return result;
  }

  /**
   * Will return an instance of this class.
   *
   * @return An instance of this class.
   */
  public static Locale getInstance()
  {
    if( instance == null )
    {
      instance = new Locale();
    }

    return instance;
  }
}