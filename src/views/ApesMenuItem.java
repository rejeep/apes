package apes.views;

import javax.swing.JMenuItem;

import apes.lib.Locale;

/**
 * This extends {@link javax.swing.JMenuItem JMenuItem} with locale
 * functionality.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesMenuItem extends JMenuItem
{
  /**
   * Locale object.
   */
  private Locale locale;

  /**
   * Creates a new <code>ApesMenuItem</code> instance.
   *
   * @param tag The locale tag.
   */
  public ApesMenuItem( String tag )
  {
    locale = Locale.getInstance();

    setText( locale.get( tag ) );
  }
}