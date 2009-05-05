package apes.views;

import apes.interfaces.LanguageObserver;
import apes.lib.Language;
import javax.swing.JButton;

/**
 * Like JButton except that it takes locale tag as argument instead of
 * the text.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesButton extends JButton implements LanguageObserver
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
  public ApesButton( String tag )
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