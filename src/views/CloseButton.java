package apes.views;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * 
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class CloseButton extends JButton
{
  /**
   * 
   */
  private ButtonTabPanel buttonTabPanel;
  
  /**
   * 
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
   *
   */
  public ButtonTabPanel getButtonTabPanel()
  {
    return this.buttonTabPanel;
  }
}