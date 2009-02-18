package src.app.views;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * This class holds all help related view stuff.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class HelpView
{
  /**
   * Shows a pop-up message with some about information.
   */
  public void about()
  {
    JOptionPane.showMessageDialog( new JLabel(  ),
                                   "apes - Audio Program for Editing Sound\n" +
                                   "Version: x\n" +
                                   "Authors: Johan Andersson, Daniel Kvick, Johan Ålander, Sophie Kores and Simon Holm",
                                   "About apes", JOptionPane.INFORMATION_MESSAGE,
                                   new ImageIcon( "images/apes.png" ) );
  }
}
