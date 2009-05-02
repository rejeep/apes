package apes.views;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A tab close button.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class CloseButton extends JButton
{
  /**
   * The panel the button is placed on.
   */
  private ButtonTabPanel buttonTabPanel;


  /**
   * Creates a new <code>CloseButton</code> instance.
   *
   * @param buttonTabPanel The panel the button is placed on.
   */
  public CloseButton( ButtonTabPanel buttonTabPanel )
  {
    this.buttonTabPanel = buttonTabPanel;

    ImageIcon icon = new ImageIcon( "images/close_tab.png" );

    int width = icon.getIconWidth();
    int height = icon.getIconHeight();

    setIcon( icon );

    setPreferredSize( new Dimension( width, height ) );

    setToolTipText( "Close this tab" );
    setContentAreaFilled( false );
    setFocusable( false );
    setBorderPainted( false );
    setContentAreaFilled( false );
  }

  /**
   * Returns the panel this button is placed on.
   *
   * @return The panel.
   */
  public ButtonTabPanel getButtonTabPanel()
  {
    return this.buttonTabPanel;
  }
}