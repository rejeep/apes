package apes.controllers;

import apes.lib.Language;

/**
 * This class handles the program languages. For example it switches
 * between them.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class LanguageController extends ApplicationController
{
  /**
   * A language object.
   */
  private Language language;


  /**
   * Creates a new <code>LanguageController</code> instance.
   */
  public LanguageController()
  {
    this.language = Language.getInstance();
  }

  @Override
  public void methodMissing()
  {
    // Set new language.
    language.setLanguage( name );

    try
    {
      language.load();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }
}
