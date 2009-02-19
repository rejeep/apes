package apes.views;

import javax.swing.JMenu;

import apes.lib.Language;

/**
 * This extends {@link javax.swing.JMenuItem JMenu} with locale
 * functionality.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesMenu extends JMenu
{
  /**
   * Creates a new <code>ApesMenu</code> instance.
   *
   * @param tag The Language tag.
   */
  public ApesMenu( String tag )
  {
    setText( Language.get( tag ) );
  }
}