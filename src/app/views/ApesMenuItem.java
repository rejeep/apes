package src.app.views;

import src.app.helpers.Language.Language;
import src.app.helpers.ApesObserver;

import javax.swing.*;

/**
 * This extends {@link javax.swing.JMenuItem JMenuItem} with locale
 * functionality.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesMenuItem extends JMenuItem implements ApesObserver
{
  private String tag;
  /**
   * Creates a new <code>ApesMenuItem</code> instance.
   *
   * @param tag The Language tag.
   */
  public ApesMenuItem( String tag )
  {
    this.tag = tag;
    setText( Language.get( tag ) );
    Language.addObserver( this );
  }

  /**
   * Update method used to update the text on the item.
   */
  public void update()
  {    
    setText( Language.get( tag ) );
    this.updateUI();
  }
}