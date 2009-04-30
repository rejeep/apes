package apes.controllers;

import apes.lib.Language;

/**
 *
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
   *
   */
  public LanguageController()
  {
    this.language = Language.getInstance();
  }

  @Override
  public void methodMissing()
  {
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
