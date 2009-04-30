package apes.views;

import javax.swing.JMenu;

import apes.interfaces.LanguageObserver;
import apes.lib.Language;

/**
 * This extends {@link javax.swing.JMenuItem JMenu} with locale
 * functionality.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesMenu extends JMenu implements LanguageObserver
{
  /**
   * TODO:
   */
  private String tag;
  
  /**
   * A language object.
   */
  private Language language;
  
  /**
   * Creates a new <code>ApesMenu</code> instance.
   *
   * @param tag The Language tag.
   */
  public ApesMenu( String tag )
  {
    this.tag = tag;
    this.language = Language.getInstance();
    
    setText( language.get( tag ) );
    language.addObserver(this);
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