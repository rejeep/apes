package apes.views.tabs;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import apes.lib.Language;

/**
 * A tab close button.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class TabCloseButton extends JButton
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
  public TabCloseButton(ButtonTabPanel buttonTabPanel)
  {
    this.buttonTabPanel = buttonTabPanel;

    ImageIcon icon = new ImageIcon("images/close_tab.png");

    int width = icon.getIconWidth();
    int height = icon.getIconHeight();

    setIcon(icon);

    setPreferredSize(new Dimension(width, height));

    Language language = Language.getInstance();
    setToolTipText(language.get("tabs.close"));
    setContentAreaFilled(false);
    setFocusable(false);
    setBorderPainted(false);
    setContentAreaFilled(false);
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
