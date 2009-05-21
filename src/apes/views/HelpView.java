package apes.views;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import apes.lib.Language;


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
    Language language = Language.getInstance();

    JOptionPane.showMessageDialog(null, language.get("help.about.name") + "\n" + language.get("help.about.authors"), language.get("help.about.title"), JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/apes.png"));
  }
}
