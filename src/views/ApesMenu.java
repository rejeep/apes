package apes.views;

import javax.swing.JMenu;

import apes.lib.Locale;

/**
 * This extends {@link javax.swing.JMenuItem JMenu} with locale
 * functionality.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesMenu extends JMenu
{
  /**
   * Locale object.
   */
  private Locale locale;

  /**
   * Creates a new <code>ApesMenu</code> instance.
   *
   * @param tag The locale tag.
   */
  public ApesMenu( String tag )
  {
    locale = Locale.getInstance();

    setText( locale.get( tag ) );
  }
}