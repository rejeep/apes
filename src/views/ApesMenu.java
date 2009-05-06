package apes.views;

import javax.swing.JMenu;

import apes.interfaces.LanguageObserver;
import apes.lib.Language;
import java.util.Observable;

/**
 * This extends {@link javax.swing.JMenuItem JMenu} with locale
 * functionality.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesMenu extends JMenu implements LanguageObserver
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
  
  public void update( Observable o, Object arg )
  {
    setText( language.get( tag ) );
    this.updateUI();
  }
}