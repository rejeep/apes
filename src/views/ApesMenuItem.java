package apes.views;

import javax.swing.JMenuItem;

import apes.interfaces.LanguageObserver;
import apes.lib.Language;

/**
 * This extends {@link javax.swing.JMenuItem JMenuItem} with locale
 * functionality.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesMenuItem extends JMenuItem implements LanguageObserver
{
  /**
   * The locale tag.
   */
  private String tag;

  /**
   * A language object.
   */
  private Language language;

  /**
   * Creates a new <code>ApesMenuItem</code> instance.
   *
   * @param tag The Language tag.
   */
  public ApesMenuItem( String tag )
  {
    this.language = Language.getInstance();
    this.tag = tag;

    setText( language.get( tag ) );
    language.addObserver( this );
  }

  /**
   * Update method used to update the text on the item.
   */
  public void update()
  {
    setText( language.get( tag ) );

    this.updateUI();
  }
}