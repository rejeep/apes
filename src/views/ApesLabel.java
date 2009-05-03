package apes.views;

import javax.swing.JLabel;
import apes.interfaces.LanguageObserver;
import apes.lib.Language;

/**
 * 
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesLabel extends JLabel implements LanguageObserver
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
   * Creates a new <code>ApesPanel</code> instance.
   *
   * @param tag The Language tag.
   */
  public ApesLabel( String tag )
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