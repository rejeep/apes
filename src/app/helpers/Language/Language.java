package src.app.helpers.Language;

import java.util.Observable;
import java.util.Observer;

/**
 * Singleton class for setting the language of the application
 *
 * @author Simon Holm
 */
public class Language extends Observable
{
  private static Language instance = null;

  private static ApeLang dictionary;

  private static String path = "locales";
  private static String file = "en";

  private Language()
  {
  }

  private Language( String language )
  {
    file = language;
  }

  /**
   * Creates a Language instence with the default language.
   */
  public static void initLanguage()
  {
    if ( instance == null )
      instance = new Language();
  }

  /**
   * Creates a Language instance with the specified language.
   *
   * @param language The langage to be initiated.
   */
  public static void initLanguage( String language )
  {
    if ( instance == null )
      instance = new Language( language );
  }

  /**
   * Gets the singleton instance.
   * @return Returns an instance of Language.
   */
  /*
  public static Language getInstance()
  {
    return instance;
  }
     */

  /**
   * Sets the language
   *
   * @param language The language to change to.
   */
  public void setLanguage( String language )
  {
    file = language;
    //TODO: find out if the language exists otherwise throw exception
  }

  /**
   * Gets the text for the key.
   *
   * @param key The key corrsponding to the text.
   * @return Returns the string to the corresponhing key.
   */
  public static String get( String key )
  {
    return dictionary.get( key );
  }

  /**
   * Loads the dictionary into the memory from the file specivied.
   *
   * @throws Exception Throws an exception if the parsing goes wrong.
   */
  public static void load() throws Exception
  {
    dictionary = new ApeLang( path, file + ".yml" );
    instance.notifyObservers();
  }

  /**
   * Adds an observer to the observable instance of Language.
   *
   * @param observer The observable item that should be added to the observable.
   */
  public static void follow( Observer observer )
  {
    instance.addObserver( observer );
  }
}
