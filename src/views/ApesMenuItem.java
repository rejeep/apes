package apes.views;

import javax.swing.JMenuItem;

import apes.lib.Language;

/**
 * This extends {@link javax.swing.JMenuItem JMenuItem} with locale
 * functionality.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesMenuItem extends JMenuItem
{
  /**
   * Creates a new <code>ApesMenuItem</code> instance.
   *
   * @param tag The Language tag.
   */
  public ApesMenuItem( String tag )
  {
    setText( Language.get( tag ) );
  }
}