package apes.lib;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Observable;

/**
 * Singleton class for setting the language of the application also
 * works as an observable for ApesObserver.
 * 
 * @author Simon Holm
 */
public class Language extends Observable
{
  /**
   * The default language to be loaded.
   */
  private static final String DEFAULT_LANGUAGE = "en";

  /**
   * The Language instance.
   */
  private static Language instance = null;

  /**
   * The dictionary contianing all the words.
   */
  private ApeLang dictionary;

  /**
   * The path to the folder with the language files.
   */
  private String path = "locales";

  /**
   * The name of the language file without the extenison.
   */
  private String language;

  /**
   * Creates a new <code>Language</code> instance. Private so that
   * you cant create an object of this class without using {@link
   * Language#getInstance getInstance}.
   */
  private Language()
  {
    language = DEFAULT_LANGUAGE;
  }

  /**
   * Sets the language
   * 
   * @param language The language to change to.
   */
  public void setLanguage(String language)
  {
    String[] languages = getLanguages();
    Arrays.sort(languages);

    int index = Arrays.binarySearch(languages, language);

    if(index >= 0)
    {
      this.language = language;
    }
    else
    {
      this.language = DEFAULT_LANGUAGE;

      System.err.println("Cannot set language " + language + ". Locale does not exist.");
    }

    setChanged();
  }
  
  /**
   * Returns the currently set language.
   * 
   * @return Returns the string for the set locale.
   */
  public String getLanguage()
  {
    return language;
  }

  /**
   * Gets the text for the key.
   * 
   * @param key The key corrsponding to the text.
   * @return Returns the string to the corresponhing key.
   */
  public String get(String key)
  {
    String result = dictionary.get(key);

    if(result == null)
    {
      System.err.println("Key " + key + " does not exist in locale " + language);

      return key;
    }

    return result;
  }

  /**
   * Loads the dictionary into the memory from the file specified.
   * This will also notify all the observers looking at Language.
   * 
   * @throws Exception Throws an exception if the parsing goes wrong.
   */
  public void load() throws Exception
  {
    dictionary = new ApeLang(path, language + ".yml");

    notifyObservers();
  }

  /**
   * Returns an array of all available languages. The array will
   * contain the name of the locale. For example: [ "en", "sv", "dk" ]
   * 
   * @return All available languages.
   */
  public String[] getLanguages()
  {
    String[] files = new File(path).list(new LanguageFileFilter());
    String[] result = new String[files.length];

    Pattern pattern = Pattern.compile("(.*)\\.yml$");
    Matcher matcher;

    for(int i = 0; i < files.length; i++)
    {
      matcher = pattern.matcher(files[i]);

      if(matcher.matches())
      {
        result[i] = matcher.group(1);
      }
    }

    return result;
  }

  /**
   * Will return an instance of this class.
   * 
   * @return An instance of this class.
   */
  public static Language getInstance()
  {
    if(instance == null)
    {
      instance = new Language();
    }

    return instance;
  }

  /**
   * Filter for language files.
   */
  private class LanguageFileFilter implements FilenameFilter
  {
    /**
     * Returns true if the given file matches a valid language file.
     * False otherwise.
     * 
     * @param dir The directory.
     * @param name The file name.
     * @return True if valid. False otherwise.
     */
    public boolean accept(File dir, String name)
    {
      return name.endsWith(".yml");
    }
  }
}
