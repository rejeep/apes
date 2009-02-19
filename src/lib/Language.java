package apes.lib;

import java.util.ArrayList;
import java.util.List;

import apes.lib.ApeLang;
import apes.lib.Interfaces;

/**
 * Singleton class for setting the language of the application
 *
 * @author Simon Holm
 */
public class Language
{
  private static Language instance = null;

  private static ApeLang dictionary;

  private static String path = "locales";
  private static String file = "en";

  private static List<ApesObserver> observers = new ArrayList<ApesObserver>();

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
   * Sets the language
   *
   * @param language The language to change to.
   */
  public static void setLanguage( String language )
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
   * This will also notify all the observers looking at Language.
   *
   * @throws Exception Throws an exception if the parsing goes wrong.
   */
  public static void load() throws Exception
  {
    dictionary = new ApeLang( path, file + ".yml" );
    notifyAllObservers();
  }

  /**
   * Adds an observer to the Language.
   *
   * @param observer The observable item that should be added to watch the language.
   */
  public static void addObserver( ApesObserver observer )
  {
    observers.add( observer );
  }

  /**
   * Removes the specified observer form the language.
   * @param observer The observer to be removed.
   */
  public static void removeObserver( ApesObserver observer )
  {
    observers.remove(observer);
  }

  private static void notifyAllObservers()
  {
    for(ApesObserver o: observers)
      o.update();
  }
}
